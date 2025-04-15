// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.properties.BatchProperty;
import com.spatialid.app.manager.IBatchUtilityManager;

/**
 * {@link Tasklet}の実装クラス．<br>
 * ファイルの削除を行うクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/26
 */
@Component
@StepScope
public class DeleteFilesTasklet implements Tasklet {
    
    /**
     * 実行パラメータのタスクID
     */
    @Value("#{jobParameters[taskId]}")
    private String taskId;
    
    /**
     * バッチのユーティリティ処理を実装したコンポーネント．
     */
    private final IBatchUtilityManager batchUtilityService;
    
    /**
     * バッチのプロパティクラス．
     */
    private final BatchProperty batchProperty;
    
    public DeleteFilesTasklet(IBatchUtilityManager batchUtilityService,
            BatchProperty batchProperty) {
        
        this.batchUtilityService = batchUtilityService;
        this.batchProperty = batchProperty;
        
    }
    
    /**
     * {@link Tasklet#execute(StepContribution, ChunkContext)}の実装を行う．
     * <p>
     * ファイル削除を行う．
     * </p>
     * 
     * @param contribution {@link StepContribution}
     * @param context {@link ChunkContext}
     * @return ステップのステータス
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) {
        
        StringBuilder jsonPathBuilder = new StringBuilder();
        
        String jsonPath = jsonPathBuilder.append(batchProperty.getExportPath())
                .append("/")
                .append(taskId)
                .append(BatchCommonConstant.EXPORT_FILE_SURFIX)
                .append(BatchCommonConstant.EXPORT_FILE_EXTENTION)
                .toString();
            
        StringBuilder zipFilePathBuilder = new StringBuilder();
        
        String zipPath = zipFilePathBuilder.append(batchProperty.getExportPath())
            .append("/")
            .append(taskId)
            .append(BatchCommonConstant.EXPORT_FILE_SURFIX)
            .append(BatchCommonConstant.FILE_COMPRESSION_FORMAT)
            .toString();
        
        // ファイル削除を実行
        batchUtilityService.deleteFiles(jsonPath, zipPath);
        
        return RepeatStatus.FINISHED;
        
    }
    
}
