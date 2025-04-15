// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.tasklet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.exception.RetryableException;
import com.spatialid.app.common.properties.BatchProperty;
import com.spatialid.app.dto.ParameterDto;
import com.spatialid.app.dto.outputtasks.GetOutputTasksRequestDto;
import com.spatialid.app.dto.outputtasks.GetOutputTasksResponseDto;
import com.spatialid.app.dto.outputtasks.OutputTasksDto;
import com.spatialid.app.dto.outputtasks.PutOutputTasksRequestDto;
import com.spatialid.app.dto.outputtasks.TaskRequestDto;
import com.spatialid.app.dto.s3.PostS3ResponseDto;
import com.spatialid.app.dto.sidattribute.PostSidAttributeRequestDto;
import com.spatialid.app.dto.sidattribute.PostSidAttributeResponseDto;
import com.spatialid.app.manager.IBatchUtilityManager;
import com.spatialid.app.manager.ICommonApiManager;

/**
 * {@link Tasklet}の実装クラス．<br>
 * 埋設物情報取得処理を定義したクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/17
 */
@Component
@StepScope
public class GetUndergroundFacilityTasklet implements Tasklet {
    
    /**
     * 実行パラメータのマップ．
     */
    private Map<String, Object> paramMap;
    
    /**
     * JSONオブジェクトマッパー．
     */
    private final ObjectMapper objectMapper;
    
    /**
     * バッチのプロパティクラス．
     */
    private final BatchProperty batchProperty;
    
    /**
     * 共通APIの呼び出しを実装したコンポーネント．
     */
    private final ICommonApiManager commonApiService;
    
    /**
     * バッチのユーティリティ処理を実装したコンポーネント．
     */
    private final IBatchUtilityManager batchUtilityService;
    
    /**
     * リトライ処理のハンドラー．
     */
    private final RetryTemplate retryTemplate;
    
    public GetUndergroundFacilityTasklet(ObjectMapper objectMapper,
            BatchProperty batchProperty,
            ICommonApiManager commonApiService,
            IBatchUtilityManager batchUtilityService,
            RetryTemplate retryTemplate) {
        
        this.objectMapper = objectMapper;
        this.batchProperty = batchProperty;
        this.commonApiService = commonApiService;
        this.batchUtilityService = batchUtilityService;
        this.retryTemplate = retryTemplate;
        
    }
    
    /**
     * {@link Tasklet#execute(StepContribution, ChunkContext)}の実装を行う．
     * <p>
     * {@link RetryTemplate#execute(RetryCallback)}内で処理を行うことで、リトライの制御をSpring Retryに移譲する．
     * </p>
     * 
     * @param stepContribution {@link StepContribution}
     * @param context {@link ChunkContext}
     * @return ステップのステータス
     */
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext context) {
        
        // パラメータを取得
        final Map<String, JobParameter<?>> parameters = context.getStepContext()
                .getStepExecution()
                .getJobParameters()
                .getParameters();
        
        paramMap = parameters.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));
        
        RepeatStatus repeatStatus = RepeatStatus.FINISHED;
        
        // 処理中に例外が発生した場合、ポリシーに則ってリトライが行われる
        repeatStatus = retryTemplate.execute(
                new RetryCallback<RepeatStatus, RuntimeException>() {
                    
                    @Override
                    public RepeatStatus doWithRetry(RetryContext context) {
                        try {
                            
                            return getUndergroundFacility();
                            
                        } catch (RetryableException e) {
                            
                            throw e;
                            
                        }
                        
                    }
            
                }
                
            );
        
        return repeatStatus;
        
    }
    
    /**
     * 埋設物情報取得処理を定義する．
     * 
     * @return ステップのステータス
     * @throws RetryableException
     */
    private RepeatStatus getUndergroundFacility() throws RetryableException {
        
        try {
            
            final ParameterDto parameterDto = objectMapper.convertValue(paramMap, ParameterDto.class);
            
            batchUtilityService.validateTaskId(parameterDto);
            
            final GetOutputTasksRequestDto getOutputTasksRequestDto = GetOutputTasksRequestDto.builder()
                    .taskId(parameterDto.getTaskId())
                    .processClass(BatchCommonConstant.PROCESS_CLASS)
                    .build();
            
            // 設備データ出力タスク参照APIの呼び出し
            final GetOutputTasksResponseDto getOutputTasksResponseDto = commonApiService.callGetOutputTasks(getOutputTasksRequestDto);
            
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(BatchCommonConstant.DATE_TIME_FORMAT);
            
            final LocalDateTime now = LocalDateTime.now();
            
            final PutOutputTasksRequestDto putTaskProgressDto = PutOutputTasksRequestDto.builder()
                    .taskId(parameterDto.getTaskId())
                    .taskStatus(BatchCommonConstant.TASK_STATUS_PROCESSING)
                    .taskStartDate(now.format(dateTimeFormatter))
                    .build();
            
            // 設備データ出力タスク更新APIの呼び出し
            commonApiService.callPutOutputTasks(putTaskProgressDto);
            
            //バッチでは1件のみ返却される想定のため、最初のタスクを抽出
            OutputTasksDto outputTasksDto = getOutputTasksResponseDto.getTaskList().getFirst();
            
            // 立体状態の空間IDを抽出
            final List<String> extrudedSids = outputTasksDto.getRequest().getSidList();
            
            // 空間IDの平面化を行う
            final List<String> flattenedSids = batchUtilityService.flattenSids(extrudedSids);
            
            final TaskRequestDto taskRequestDto = outputTasksDto.getRequest();
            
            final PostSidAttributeRequestDto postSidAttributeRequestDto = PostSidAttributeRequestDto.builder()
                    .sidList(flattenedSids)
                    .isFlatSearch(true)
                    .servicerId(outputTasksDto.getServicerId())
                    .infraCompanyIdList(taskRequestDto.getInfraCompanyIdList())
                    .updateTime(taskRequestDto.getUpdateDate())
                    .returnZoomLevel(taskRequestDto.getReturnZoomLevel())
                    .isBatchProcess(true)
                    .build();
            
            // 空間・属性情報参照APIの呼び出し
            final PostSidAttributeResponseDto postSidAttributeResponseDto = commonApiService.callSidAttribute(postSidAttributeRequestDto);
            
            StringBuilder jsonPathBuilder = new StringBuilder();
            
            //出力するファイルのパスを設定
            final String jsonPath = jsonPathBuilder.append(batchProperty.getExportPath())
                .append("/")
                .append(parameterDto.getTaskId())
                .append(BatchCommonConstant.EXPORT_FILE_SURFIX)
                .append(BatchCommonConstant.EXPORT_FILE_EXTENTION)
                .toString();
            
            StringBuilder zipFilePathBuilder = new StringBuilder();
            
            //出力したファイルの圧縮先を設定
            final String zipPath = zipFilePathBuilder.append(batchProperty.getExportPath())
                .append("/")
                .append(parameterDto.getTaskId())
                .append(BatchCommonConstant.EXPORT_FILE_SURFIX)
                .append(BatchCommonConstant.FILE_COMPRESSION_FORMAT)
                .toString();
            
            // ファイル作成を行う
            batchUtilityService.createFile(postSidAttributeResponseDto.getSidAttributeList(),
                    jsonPath,
                    zipPath);
            
            // multipart/form-dataの送信データを作成
            MultiValueMap<String, Object> postS3RequestMap = new LinkedMultiValueMap<String, Object>();
            
            postS3RequestMap.set("uploadFile", batchUtilityService.getFileAsResource(zipPath));
            postS3RequestMap.set("taskId", parameterDto.getTaskId());
            postS3RequestMap.set("servicerId", outputTasksDto.getServicerId());
            
            // S3格納APIを呼び出し
            final PostS3ResponseDto postS3ResponseDto = commonApiService.callFileToS3(postS3RequestMap);
            
            final PutOutputTasksRequestDto putTaskCompleteDto = PutOutputTasksRequestDto.builder()
                    .taskId(parameterDto.getTaskId())
                    .taskStatus(BatchCommonConstant.TASK_STATUS_COMPLETE)
                    .fileUrl(postS3ResponseDto.getFilePath())
                    .build();
            
            // 設備データ出力タスク更新APIの呼び出し(ステータスを完了に更新)
            commonApiService.callPutOutputTasks(putTaskCompleteDto);
            
            return RepeatStatus.FINISHED;

        } catch (RetryableException e) {
            
            throw e;
            
        }
        
    }
        
}
