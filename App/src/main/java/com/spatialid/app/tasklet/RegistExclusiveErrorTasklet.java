// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.dto.outputtasks.PutOutputTasksRequestDto;
import com.spatialid.app.manager.ICommonApiManager;

/**
 * {@link Tasklet}の実装クラス．<br>
 * 特殊例外の処理を定義したクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
@Component
@StepScope
public class RegistExclusiveErrorTasklet implements Tasklet {
    
    /**
     * 実行パラメータのタスクID
     */
    @Value("#{jobParameters[taskId]}")
    private String taskId;
    
    /**
     * 共通APIの呼び出しを実装したコンポーネント．
     */
    private final ICommonApiManager commonApiService;
    
    public RegistExclusiveErrorTasklet(ICommonApiManager commonApiService) {
        
        this.commonApiService = commonApiService;
        
    }
    
    /**
     * {@link Tasklet#execute(StepContribution, ChunkContext)}の実装を行う．
     * <p>
     * 特殊例外の処理を行う．<br>
     * 特殊例外の内容と共に、設備データ出力タスク管理テーブルにステータスをエラーで更新する．
     * </p>
     * 
     * @param stepContribution {@link StepContribution}
     * @param context {@link ChunkContext}
     * @return ステップのステータス
     */
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext context) {
        
        ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        
        final String errorMsg = executionContext.getString(BatchCommonConstant.CONTEXT_KEY_ERROR_MSG);
        
        final PutOutputTasksRequestDto putTaskExclusiveErrorDto = PutOutputTasksRequestDto.builder()
                .taskId(taskId)
                .taskStatus(BatchCommonConstant.TASK_STATUS_ERROR)
                .errorDetail(errorMsg)
                .build();
        
        commonApiService.callPutOutputTasks(putTaskExclusiveErrorDto);
        
        return RepeatStatus.FINISHED;
        
    }
    
}
