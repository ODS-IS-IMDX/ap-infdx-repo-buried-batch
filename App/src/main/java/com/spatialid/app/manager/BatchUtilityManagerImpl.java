// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.manager;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spatialid.app.common.benchmark.annotation.MeasureLatency;
import com.spatialid.app.common.exception.NoRetryableException;
import com.spatialid.app.common.exception.NoRetryableFileProcessingException;
import com.spatialid.app.common.exception.NoRetryableParamErrorException;
import com.spatialid.app.common.exception.RetryableException;
import com.spatialid.app.common.exception.RetryableFileProcessingException;
import com.spatialid.app.common.exception.RetryableS3Exception;
import com.spatialid.app.common.exception.RetryableSidAttributeException;
import com.spatialid.app.common.printer.LineFeedPrinter;
import com.spatialid.app.dto.ParameterDto;
import com.spatialid.app.dto.sidattribute.SidAttributeDto;
import com.spatialid.app.entity.ExportJsonEntity;
import com.spatialid.app.entity.SidAttributeEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * バッチのユーティリティ処理を定義した{@link IBatchUtilityManager}を実装するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/19
 */
@Component
public class BatchUtilityManagerImpl implements IBatchUtilityManager {
    
    /**
     * Jsonオブジェクトマッパー．
     */
    private final ObjectMapper objectMapper;
    
    /**
     * バリデータ．
     */
    private final Validator validator;
    
    public BatchUtilityManagerImpl(ObjectMapper objectMapper,
            Validator validator) {
        
        this.objectMapper = objectMapper;
        this.validator = validator;
        
    }
    
    /**
     * パラメータとして渡されたタスクIDのバリデーションを実行するメソッド．
     * 
     * @param parameterDto タスクIDを格納したDTO．
     * @throws NoRetryableException バリデーション違反を検出した場合
     */
    @Override
    public void validateTaskId(ParameterDto parameterDto) throws NoRetryableException {
        
        Set<ConstraintViolation<Object>> violations = validator.validate(parameterDto);
        
        Map<String, String> violationMap = new HashMap<String, String>();
        
        violations.forEach(violation -> {
            
            violationMap.put(violation.getPropertyPath().toString(),
                    Optional.ofNullable(violation.getInvalidValue())
                        .map(Object::toString)
                        .orElse(null));
            
        });
        
        if (!(violationMap.isEmpty())) {
            
            throw new NoRetryableParamErrorException(violationMap);
            
        }
        
    }
    
    /**
     * 空間IDのリストを全て平面化する．
     * 
     * @param extrudedSids 鉛直方向の情報が存在する空間IDのリスト
     * @return 平面化された空間IDのリスト
     * @throws RetryableException 処理に失敗した場合
     */
    @Override
    @MeasureLatency
    public List<String> flattenSids(List<String> extrudedSids) throws RetryableException {
        
        try {
            
            List<String> flattenedSids = extrudedSids.stream()
                    .map(this::flattenSid)
                    .collect(Collectors.toList());
            
            return flattenedSids;
            
        } catch (Exception e) {
            
            throw new RetryableSidAttributeException(e.getMessage());
            
        }
        
    }
    
    /**
     * 空間・属性情報リストを元にファイルを作成する．
     * 
     * @param sidAttributeList 空間・属性情報リスト
     * @param jsonPath jsonファイルのパス
     * @param zipPath 圧縮ファイルのパス
     * @throws RetryableException 処理に失敗した場合
     */
    @Override
    @MeasureLatency
    public void createFile(List<SidAttributeDto> sidAttributeList, String jsonPath, String zipPath) throws RetryableException {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(jsonPath, StandardCharsets.UTF_8))) {
            
            boolean isFacilityExist = false;
            
            if (sidAttributeList.size() != 0) {
                
                isFacilityExist = true;
                
            }
            
            final List<SidAttributeEntity> sidAttributeEntities = sidAttributeList.stream()
                    .map(source -> SidAttributeEntity.builder()
                            .infraCompanyId(source.getInfraCompanyId())
                            .dataType(source.getDataType())
                            .objectId(source.getObjectId())
                            .objectName(source.getFacilityClassificationName())
                            .facilityAttribute(source.getFacilityAttribute())
                            .sidList(source.getSidList())
                            .build())
                    .collect(Collectors.toList());
            
            final ExportJsonEntity exportJsonEntity = ExportJsonEntity.builder()
                    .isFacilityExist(isFacilityExist)
                    .facilitySidList(sidAttributeEntities)
                    .build();
                        
            // Jsonのインデントを有効化
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            
            // json出力時のインデント/改行コードを設定
            objectMapper.setDefaultPrettyPrinter(new LineFeedPrinter());
            
            objectMapper.writeValue(bw, exportJsonEntity);
            
            // 圧縮を行う
            compressJsonFile(jsonPath, zipPath);
            
        } catch (Exception e) {
            
            throw new RetryableFileProcessingException(e.getMessage());
            
        }
        
    }
    
    /**
     * ファイルを{@link Resource}として取得する．
     * 
     * @param path 取得対象のパス．
     * @return {@link Resource}で取得されたファイル
     * @throws RetryableException 処理に失敗した場合
     */
    @Override
    public Resource getFileAsResource(String path) throws RetryableException {
        
        try {
            
            Path sourcePath = Path.of(path);
            
            Resource resource = new FileSystemResource(sourcePath);
            
            return resource;
            
        } catch (Exception e) {
            
            throw new RetryableS3Exception(e.getMessage());
            
        }
        
    }
    
    /**
     * json、zipファイルの削除を行う．
     * <p>
     * {@link IBatchUtilityManager#createFile(List, String, String)}で作成されたファイルを全て削除する．
     * </p>
     * 
     * @param jsonPath jsonファイルのパス
     * @param zipPath 圧縮ファイルのパス
     * @throws NoRetryableException リトライ処理を行わず、異常終了することを示す例外
     */
    @Override
    public void deleteFiles(String jsonPath, String zipPath) throws NoRetryableException {
        
        try {
                    
            Path sourcePath = Path.of(jsonPath.toString());
            
            Path compressPath = Path.of(zipPath.toString());
        
            Files.deleteIfExists(sourcePath);
            
            Files.deleteIfExists(compressPath);
            
        } catch (Exception e) {
            
            throw new NoRetryableFileProcessingException(e.getMessage());
            
        }
        
    }
        
    /**
     * 空間IDを平面化する．
     * <p>
     * &quot;/&quot;で分割した文字列の第二要素を&quot;0&quot;に置換する．
     * </p>
     * 
     * @param extrudedSid 鉛直方向の情報が存在する空間ID
     * @return 平面化された空間ID
     */
    private String flattenSid(String extrudedSid) {
        
        String[] splitedSid = extrudedSid.split("/");
        
        splitedSid[1] = "0";
        
        return String.join("/", splitedSid);
                
    }
    
    /**
     * 指定されたファイルをzip形式で圧縮する．
     * 
     * @param sourcePathStr 圧縮対象となるファイルのパス(文字列)
     * @param zipPathStr 圧縮されたファイルの出力パス
     * @throws Exception 処理に失敗した場合
     */
    private void compressJsonFile(String sourcePathStr, String zipPathStr) throws Exception {
        
        Path sourcePath = Path.of(sourcePathStr);
        
        Path zipPath = Path.of(zipPathStr);
        
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipPath.toFile()));
                ZipOutputStream zos = new ZipOutputStream(bos);
           ) {
            
            ZipEntry zipEntry = new ZipEntry(sourcePath.getFileName().toString());
            
            zos.putNextEntry(zipEntry);
            
            zos.write(Files.readAllBytes(sourcePath));
            
        } catch (Exception e) {
            
            throw e;
            
        }
        
    }
    
}
