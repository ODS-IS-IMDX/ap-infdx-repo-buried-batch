// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.exception.NoRetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableFileProcessingException;
import com.spatialid.app.common.exception.RetryableS3Exception;
import com.spatialid.app.common.exception.RetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableSidAttributeLockedException;
import com.spatialid.app.config.BatchConfig;

import lombok.NoArgsConstructor;

/**
 * {@link StepExecutionListener}の実装クラス．
 * ステップ終了時の例外処理を定義したクラス．<br>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/26
 */
@Component("exceptionListener")
@NoArgsConstructor
public class ExceptionListener implements StepExecutionListener {
    
    /**
     * {@link StepExecutionListener#afterStep(StepExecution)}の実装を行う．
     * <p>
     * リトライや処理失敗時に、ステップの最終的なステータスを定義とエラー内容の保存を行う．<br>
     * ここで設定した終了ステータスを元に{@link BatchConfig}で次の処理を決定する．
     * </p>
     * 
     * @param stepExecution {@link StepExecution}
     * @return ステップの終了ステータス
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        
        final BatchStatus batchStatus = stepExecution.getStatus();
        
        // バッチの処理に失敗した場合
        if (batchStatus == BatchStatus.FAILED) {
            
            // 発生した例外を採取する
            final Throwable exception = stepExecution.getFailureExceptions()
                    .stream()
                    .findFirst()
                    .orElse(null);
            
            ExitStatus exitStatus = ExitStatus.FAILED;
            
            String errorMsg;
            
            if (exception != null) {
                
                // 例外の種類を元に、終了ステータスを決定する
                exitStatus = switch(exception) {
                    
                    case RetryableSidAttributeException sidAttribute -> new ExitStatus(BatchCommonConstant.STATUS_GENERIC_ERROR);
                    
                    case RetryableS3Exception s3 -> new ExitStatus(BatchCommonConstant.STATUS_GENERIC_ERROR);
                    
                    case RetryableFileProcessingException fileProcess -> new ExitStatus(BatchCommonConstant.STATUS_GENERIC_ERROR);
                    
                    case RetryableSidAttributeLockedException sidAttributeLocked -> new ExitStatus(BatchCommonConstant.STATUS_EXCLUSIVE_ERROR);
                    
                    case NoRetryableSidAttributeException noRetry -> new ExitStatus(BatchCommonConstant.STATUS_EXCLUSIVE_ERROR);
                    
                    default -> new ExitStatus(BatchCommonConstant.STATUS_ABEND);
            
                };
                
                // 例外からエラー内容を取得
                errorMsg = switch(exception) {
                    
                    case RetryableSidAttributeException sidAttribute -> sidAttribute.getErrorDetailInRegist();
                    
                    case RetryableFileProcessingException fileProcessing -> fileProcessing.getErrorDetailInRegist();
                    
                    case RetryableS3Exception s3 -> s3.getErrorDetailInRegist();
                    
                    case RetryableSidAttributeLockedException sidAttributeLocked -> sidAttributeLocked.getErrorDetailInRegist();
                    
                    case NoRetryableSidAttributeException noRetry -> noRetry.getErrorDetailInRegist();
                    
                    default -> null;
        
                };
                
                ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
                
                // エラー内容をcontextに保存
                executionContext.putString(BatchCommonConstant.CONTEXT_KEY_ERROR_MSG, errorMsg);

            }
            
            return exitStatus;
            
        }
        
        // 例外が発生していない場合は、終了ステータスをそのまま返却する
        return stepExecution.getExitStatus();
        
    }
    
}
