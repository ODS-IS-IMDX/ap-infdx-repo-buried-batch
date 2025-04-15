// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.spatialid.app.common.constants.ExitCodeConstant;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link ExitCodeGenerator}・{@link ApplicationListener}の実装クラス．<br>
 * バッチの終了コードを管理する．<br>
 * ジョブの実行結果ではなく、SpringBootアプリケーションの実行結果を元に終了コードを決定する．
 * 
 * @author matsumoto kentaro
 * @version 1.0 2024/09/17
 */
@Component
@Setter
@NoArgsConstructor
public class ExitCodeListener implements ExitCodeGenerator, ApplicationListener<JobExecutionEvent> {
    
    /**
     * 終了コード．
     */
    private int exitCode = ExitCodeConstant.STATUS_ERROR;
    
    
    /**
     * イベント情報．
     */
    private JobExecution jobExecution;
    
    /**
     * {@link ExitCodeGenerator#getExitCode}の実装メソッド
     * 
     * @return バッチの終了コード．
     */
    @Override
    public int getExitCode() {

        return this.exitCode;

    }
    
    /**
     * {@link ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)}の実装メソッド．
     * <p>
     * SpringBootアプリケーションのイベント終了時に呼び出される．<br>
     * 実行結果が正常終了時のみフィールド上の終了コードをセットする．
     * </p>
     */
    @Override
    public void onApplicationEvent(JobExecutionEvent jobExecutionEvent) {
        
        this.jobExecution = jobExecutionEvent.getJobExecution();
        
        ExitStatus exitStatus = jobExecution.getExitStatus();
        
        if(ExitStatus.COMPLETED.equals(exitStatus)) {
            
            this.exitCode = ExitCodeConstant.STATUS_SUCCESS;
            
        }
        
    }

}
