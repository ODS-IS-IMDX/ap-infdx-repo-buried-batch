// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.manager;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatialid.app.common.benchmark.annotation.MeasureLatency;
import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.exception.NoRetryableException;
import com.spatialid.app.common.exception.NoRetryableOutputTasksException;
import com.spatialid.app.common.exception.NoRetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableException;
import com.spatialid.app.common.exception.RetryableOutputTasksException;
import com.spatialid.app.common.exception.RetryableS3Exception;
import com.spatialid.app.common.exception.RetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableSidAttributeLockedException;
import com.spatialid.app.common.properties.ApiProperty;
import com.spatialid.app.dto.ErrorResponse;
import com.spatialid.app.dto.outputtasks.GetOutputTasksRequestDto;
import com.spatialid.app.dto.outputtasks.GetOutputTasksResponseDto;
import com.spatialid.app.dto.outputtasks.PutOutputTasksRequestDto;
import com.spatialid.app.dto.s3.PostS3ResponseDto;
import com.spatialid.app.dto.sidattribute.PostSidAttributeRequestDto;
import com.spatialid.app.dto.sidattribute.PostSidAttributeResponseDto;

/**
 * 共通APIの呼び出しを定義した{@link ICommonApiManager}を実装するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/19
 */
@Component
public class CommonApiManagerImpl implements ICommonApiManager {
    
    /**
     * HTTPクライアント．
     */
    private final RestClient restClient;
    
    /**
     * Jsonオブジェクトマッパー．
     */
    private final ObjectMapper objectMapper;
    
    /**
     * api.propertiesの値を保持するクラス．
     */
    private final ApiProperty apiProperty;
    
    public CommonApiManagerImpl(RestClient restClient,
            ObjectMapper objectMapper,
            ApiProperty apiProperty) {

        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.apiProperty = apiProperty;
        
    }
    
    /**
     * 設備データ出力参照APIの呼び出しを実装する．
     *
     * @param requestDto {@link GetOutputTasksRequestDto} リクエストDto
     * @return {@link GetOutputTasksResponseDto} 設備データ出力参照APIのレスポンス
     * @exception RetryableOutputTasksException APIのレスポンスコードが、正常以外であった場合
     * @exception NoRetryableOutputTasksException APIのレスポンスコードが正常 かつ 件数が0件であった場合
     * @throws RetryableException
     * @throws NoRetryableException
     */
    @Override
    public GetOutputTasksResponseDto callGetOutputTasks(GetOutputTasksRequestDto requestDto) throws RetryableException, NoRetryableException {
        
        try {
                        
            final String pathWithQuery = addQueryString(apiProperty.getOutputtasksPath(), requestDto);
            
            Object rawResponse = restClient.method(HttpMethod.GET)
                    .uri(pathWithQuery)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange((request, response) -> {
                        
                        if (response.getStatusCode().isError()) {
                            
                            ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
                            
                            return errorResponse;
                            
                        } else {
                            
                            GetOutputTasksResponseDto getOutputTasksResponseDto = objectMapper.readValue(response.getBody(), GetOutputTasksResponseDto.class);
                            
                            return getOutputTasksResponseDto;
                            
                        }
                        
                    });
            
            //レスポンスのエラーチェック
            if (rawResponse instanceof ErrorResponse) {
                
                throw new RetryableOutputTasksException(BatchCommonConstant.MSG_INTERNAL_API_ERROR);
                
            }
            
            final GetOutputTasksResponseDto getOutputTasksResponseDto = (GetOutputTasksResponseDto) rawResponse;
            
            if (getOutputTasksResponseDto.getTaskList().size() == 0) {
                
                throw new NoRetryableOutputTasksException(BatchCommonConstant.MSG_TASKID_NOT_FOUND_ERROR);
                
            }
            
            return getOutputTasksResponseDto;

        } catch (RetryableOutputTasksException e) {
            
            throw e;
            
        } catch (NoRetryableOutputTasksException e) {
            
            throw e;
        
        } catch (Exception e) {
            
            throw new RetryableOutputTasksException(e.getMessage());
            
        }
        
    }
    
    /**
     * 設備データ出力更新APIの呼び出しを実装する．
     * <p>
     * 設備データ出力更新APIにおける正常終了時のレスポンスコードは、204の想定．
     * </p>
     * 
     * @param requestDto {@link PutOutputTasksRequestDto} リクエストDto
     * @exception RetryableOutputTasksException APIのレスポンスコードが、正常以外であった場合
     * @throws RetryableException
     */
    @Override
    public void callPutOutputTasks(PutOutputTasksRequestDto requestDto) throws RetryableException {
        
        try {
            
            final String path = apiProperty.getPutOutputtasksPath();
            
            final ResponseEntity<Void> rawResponse = restClient.method(HttpMethod.PUT)
                    .uri(uriBuilder -> 
                            uriBuilder.replacePath(path)
                            .build(requestDto.getTaskId()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestDto)
                    .retrieve()
                    .toBodilessEntity();

            if (!(rawResponse.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(204)))) {
                
                throw new RetryableOutputTasksException(BatchCommonConstant.MSG_INTERNAL_API_ERROR);
                
            }
            
        } catch (RetryableOutputTasksException e) {
            
            throw e;
            
        } catch (Exception e) {
            
            throw new RetryableOutputTasksException(e.getMessage());
            
        }
        
    }
        
    /**
     * 空間・属性情報参照APIの呼び出しを実装する．
     * <p>
     * 空間・属性情報参照APIにおける正常終了時のレスポンスコードは、200の想定．
     * </p>
     * 
     * @param requestDto {@link PostSidAttributeRequestDto} リクエストDto
     * @return {@link PostSidAttributeResponseDto} 空間・属性情報参照APIのレスポンス
     * @exception RetryableSidAttributeException APIのレスポンスコードが正常以外、データ整備内 かつ DB更新中でなかった場合
     * @exception RetryableSidAttributeLockedException APIのレスポンスコードが正常以外、かつ DB更新中であった場合
     * @exception NoRetryableSidAttributeException APIのレスポンスコードが正常以外、かつ データ整備範囲外であった場合
     * @throws RetryableException
     * @throws NoRetryableException
     */
    @Override
    @MeasureLatency
    public PostSidAttributeResponseDto callSidAttribute(PostSidAttributeRequestDto requestDto) throws RetryableException, NoRetryableException {
        
        try {
                        
            final String path = apiProperty.getSidAttributePath();
            
            Object rawResponse = restClient.method(HttpMethod.POST)
                    .uri(path)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestDto)
                    .exchange((request, response) -> {
                        
                        if (response.getStatusCode().isError()) {
                            
                            ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
                            return errorResponse;
                            
                        } else {
                            
                            PostSidAttributeResponseDto getSidAttributeResponseDto = objectMapper.readValue(response.getBody(), PostSidAttributeResponseDto.class);
                            return getSidAttributeResponseDto;
                            
                        }
                        
                    });
            
            PostSidAttributeResponseDto postSidAttributeResponseDto = null;
            
            if (rawResponse instanceof ErrorResponse) {
                
                ErrorResponse errorResponse = (ErrorResponse) rawResponse;
                
                String errMsg = errorResponse.getMessage();
                
                if (errMsg.matches(BatchCommonConstant.REGEX_DATA_LOCKED)) {
                    
                    throw new RetryableSidAttributeLockedException(errMsg);
                    
                } else if(errMsg.matches(BatchCommonConstant.REGEX_INVALID_DATA_RANGE)) {
                    
                    throw new NoRetryableSidAttributeException(errMsg);
                    
                }
                
                throw new RetryableSidAttributeException(BatchCommonConstant.MSG_INTERNAL_API_ERROR);
                
            } else {
                
                postSidAttributeResponseDto = (PostSidAttributeResponseDto) rawResponse;
                
            }
            
            return postSidAttributeResponseDto;

        } catch (RetryableSidAttributeException e) {
            
            throw e;
            
        } catch (RetryableSidAttributeLockedException e) {
            
            throw e;
            
        } catch (NoRetryableSidAttributeException e) {
            
            throw e;
            
        } catch (Exception e) {
            
            throw new RetryableSidAttributeException(e.getMessage());
            
        }
                
    }
    
    /**
     * S3格納APIの呼び出しを実装する，
     * <p>
     * ファイルを送信するため、multipart/form-data形式でリクエストを行う．<br>
     * S3格納APIにおける正常終了時のレスポンスコードは、201の想定．
     * </p>
     * @param requestMap {@link MultiValueMap} リクエストマップ
     * @return {@link PostS3ResponseDto} S3格納APIのレスポンス
     * @exception RetryableS3Exception APIのレスポンスコードが正常以外であった場合
     * @throws RetryableException
     */
    @Override
    @MeasureLatency
    public PostS3ResponseDto callFileToS3(MultiValueMap<String, Object> requestMap) throws RetryableException {
            
        try {
            
            final String path = apiProperty.getS3Path();
                        
            Object rawResponse = restClient.post()
                    .uri(path)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestMap)
                    .exchange((request, response) -> {
                        
                        if (response.getStatusCode().isError()) {
                            
                            ErrorResponse errorResponse = objectMapper.readValue(response.getBody(), ErrorResponse.class);
                            return errorResponse;
                            
                        } else {
                            
                            PostS3ResponseDto postS3ResponseDto = objectMapper.readValue(response.getBody(), PostS3ResponseDto.class);
                            return postS3ResponseDto;
                        }
                        
                    });
            
            PostS3ResponseDto postS3ResponseDto = null;
            
            if (rawResponse instanceof ErrorResponse) {
                
                throw new RetryableS3Exception(BatchCommonConstant.MSG_INTERNAL_API_ERROR);
                
            }
                
            postS3ResponseDto = (PostS3ResponseDto) rawResponse;
                
            return postS3ResponseDto;
            
        } catch (RetryableS3Exception e) {
            
            throw e;
            
        } catch (Exception e) {
            
            throw new RetryableS3Exception(e.getMessage());
            
        }
        
    }
    
    /**
     * APIのパスにクエリストリングを付与する．
     * <p>
     * DTOをクエリストリングとして、APIのパスに付与する．<br>
     * DTOがnullであった場合は、クエリストリングを付与せずにAPIのパスを返却する．<br>
     * また、ネストが存在しないDTOのみを対象としている．
     * </p>
     * @param path APIのパス
     * @param dto クエリストリングに設定するDTO
     * @return クエリストリングが付与されたAPIのパス
     * @exception Exception 処理に失敗した場合
     * @throws RetryableException
     */
    private String addQueryString(String path, Object dto) throws Exception {
        
        try {
            
            if (dto == null) {
                
                return path;
                
            }
            
            Map<String, Object> map = objectMapper.convertValue(dto, new TypeReference<Map<String, Object>>() {});
            
            Map<String, String> stringMap = map.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> Optional.ofNullable(entry.getValue()).map(Object::toString).orElse("")
                    ));
            
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
            
            multiValueMap.setAll(stringMap);
            
            URI pathWithQuery = UriComponentsBuilder.fromPath(path)
                    .queryParams(multiValueMap)
                    .build()
                    .toUri();
            
            return pathWithQuery.toString();

        } catch (Exception e) {
            
            throw e;
            
        }
        
    }
        
}
