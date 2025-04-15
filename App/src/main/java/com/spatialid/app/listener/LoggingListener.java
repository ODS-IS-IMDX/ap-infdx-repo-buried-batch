// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.listener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.exception.NoRetryableFileProcessingException;
import com.spatialid.app.common.exception.NoRetryableOutputTasksException;
import com.spatialid.app.common.exception.NoRetryableParamErrorException;
import com.spatialid.app.common.exception.NoRetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableFileProcessingException;
import com.spatialid.app.common.exception.RetryableOutputTasksException;
import com.spatialid.app.common.exception.RetryableS3Exception;
import com.spatialid.app.common.exception.RetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableSidAttributeLockedException;

import lombok.extern.log4j.Log4j2;

/**
 * {@link StepExecutionListener}, {@link JobExecutionListener}, {@link ApplicationListener}の実装を行う．<br>
 * バッチの起動、各ステップで最終的に送出された例外と、ジョブの開始・終了時刻を採取する．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/26
 */
@Component("loggingListener")
@Log4j2
public class LoggingListener implements StepExecutionListener, JobExecutionListener, ApplicationListener<ApplicationStartedEvent> {
    
    /**
     * メッセージプロパティ．
     */
    private final MessageSource messageSource;
    
    public LoggingListener(@Qualifier("reloadableResourceBundleMessageSource") MessageSource messageSource) {
        
        this.messageSource = messageSource;
        
    }
    
    /**
     * {@link ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)}の実装を行う．
     * <p>
     * アプリケーションの起動イベントをフックして、起動ログを採取する．
     * </p>
     * 
     * @param event {@link ApplicationStartedEvent}
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        
        log.info(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_BOOT,
                new String[] {BatchCommonConstant.PROJECT_NAME},
                Locale.JAPAN));
        
    }
    
    /**
     * {@link JobExecutionListener#beforeJob(JobExecution)}の実装を行う．
     * <p>
     * ジョブ開始時に、ジョブ名とパラメータを採取する．
     * </p>
     * 
     * @param jobExecution {@link JobExecution}
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        
        String jobName = jobExecution.getJobInstance().getJobName();
        
        final StringBuilder parameterBuilder = new StringBuilder();
        
        final Map<String, JobParameter<?>> parameters = jobExecution.getJobParameters().getParameters();
        
        for (Map.Entry<String, JobParameter<?>> entry : parameters.entrySet()) {
            
            parameterBuilder.append(entry.getValue());
            
        }
        
        log.info(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_START,
                new String[] {jobName, parameterBuilder.toString()},
                Locale.JAPAN));
        
    }
    
    /**
     * {@link StepExecutionListener#afterStep(StepExecution)}の実装を行う．
     * <p>
     * ステップ終了後に、最後に発生した例外を採取する．<br>
     * リトライ処理中に成功した場合は、採取されない．
     * </p>
     * 
     * @param stepExecution {@link StepExecution}
     * @return 本来の終了コードをそのまま返却する
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        
        final BatchStatus batchStatus = stepExecution.getStatus();
        
        if (batchStatus == BatchStatus.FAILED) {
            
            final Throwable exception = stepExecution.getFailureExceptions()
                    .stream()
                    .findFirst()
                    .orElse(null);
            
            
            String errorMsg;
            
            String warnMsg;
            
            if (exception != null) {
                
                // errorとして扱う例外のメッセージを取得
                errorMsg = switch(exception) {
                
                    case RetryableOutputTasksException outputTasks -> outputTasks.getErrorDetailInLog();
                    
                    case RetryableSidAttributeException sidAttribute -> sidAttribute.getErrorDetailInLog();
                    
                    case RetryableS3Exception s3 -> s3.getErrorDetailInLog();
                    
                    case RetryableFileProcessingException fileProcessing -> fileProcessing.getErrorDetailInLog();
                    
                    case NoRetryableFileProcessingException noRetryFileProcessing -> noRetryFileProcessing.getErrorDetailInLog();
                    
                    case NoRetryableParamErrorException noRetryParamError -> noRetryParamError.getErrorDetailInLog();
                    
                    default -> null;
        
                };
                
                // warnとして扱う例外のメッセージを取得
                warnMsg = switch(exception) {
                    
                    case RetryableSidAttributeLockedException sidAttributeLocked -> sidAttributeLocked.getErrorDetailInLog();
                    
                    case NoRetryableSidAttributeException noRetrySid -> noRetrySid.getErrorDetailInLog();
                    
                    case NoRetryableOutputTasksException noRetryOutoutTasks -> noRetryOutoutTasks.getErrorDetailInLog();
                                        
                    default -> null;
        
                };

                
                if (errorMsg != null) {
                    
                    log.error(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_ERROR, new String[] { errorMsg }, Locale.JAPAN), exception);
                    
                } else if(warnMsg != null) {
                    
                    log.warn(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_WARN, new String[] { warnMsg }, Locale.JAPAN), exception);
                    
                } else {
                    
                    log.error(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_ERROR, new String[] { exception.getMessage() }, Locale.JAPAN), exception);
                
                }
                
            }
            
        }
        
        return stepExecution.getExitStatus();
        
    }
    
    /**
     * {@link JobExecutionListener#afterJob(JobExecution)}の実装を行う．
     * <p>
     * ジョブ終了時に、ジョブ名と処理時間を採取する．
     * </p>
     * 
     * @param jobExecution {@link JobExecution}
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        
        final LocalDateTime startTime = jobExecution.getStartTime();
        
        final LocalDateTime endTime = jobExecution.getEndTime();
        
        final Duration duration = Duration.between(startTime, endTime);
        
        final long latency = duration.toMillis();
        
        String jobName = jobExecution.getJobInstance().getJobName();
        
        log.info(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_END, new Object[] {jobName, latency}, Locale.JAPAN));
        
    }
    
}
