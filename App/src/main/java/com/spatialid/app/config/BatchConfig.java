// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.constants.NameConstant;
import com.spatialid.app.tasklet.DeleteFilesTasklet;
import com.spatialid.app.tasklet.GetUndergroundFacilityTasklet;
import com.spatialid.app.tasklet.RegistExclusiveErrorTasklet;
import com.spatialid.app.tasklet.RegistGenericErrorTasklet;

import lombok.NoArgsConstructor;

/**
 * バッチの処理を定義するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/17
 */
@Configuration
@NoArgsConstructor
public class BatchConfig {
    
    /**
     * ジョブフローの制御を行う．
     * 
     * @param jobRepository {@link JobRepository}
     * @param getUndergroundFacilityStep リトライを行う埋設物情報取得処理を定義したステップ．
     * @param genericErrorFlow 普通例外を処理するサブフロー
     * @param exclusiveErrorFlow 特殊例外を処理するサブフロー
     * @param deleteFilesStep ファイル削除を行うステップ
     * @param loggingListener ロギングを行うリスナー
     * @return {@link Job}
     */
    @Bean(name = NameConstant.GET_UF_JOB_NAME)
    public Job getUndergroundFacilityJob(JobRepository jobRepository,
            Step getUndergroundFacilityStep,
            Step deleteFilesStep,
            Flow genericErrorFlow,
            Flow exclusiveErrorFlow,
            JobExecutionListener loggingListener) {
        
        return new JobBuilder(NameConstant.GET_UF_JOB_NAME, jobRepository)
                .start(getUndergroundFacilityStep).on(BatchCommonConstant.STATUS_ABEND).fail()
                .from(getUndergroundFacilityStep).on(BatchCommonConstant.STATUS_GENERIC_ERROR).to(genericErrorFlow).on("*").fail()
                .from(getUndergroundFacilityStep).on(BatchCommonConstant.STATUS_EXCLUSIVE_ERROR).to(exclusiveErrorFlow).on("*").fail()
                .from(getUndergroundFacilityStep).on("*").to(deleteFilesStep).end()
                .listener(loggingListener)
                .build();
        
    }
    
    /**
     * サブフローの制御を行う．
     * <p>
     * 普通例外が送出された場合のサブフローを定義する．
     * </p>
     * 
     * @param registGenericErrorStep 普通例外を処理するステップ
     * @param deleteFilesStep ファイル削除を行うステップ
     * @return 普通例外が送出された場合のサブフロー
     */
    @Bean(name = NameConstant.GENERIC_ERROR_FLOW_NAME)
    public Flow genericErrorFlow(Step registGenericErrorStep,
            Step deleteFilesStep) {
        
        return new FlowBuilder<Flow>(NameConstant.GENERIC_ERROR_FLOW_NAME)
                .start(registGenericErrorStep)
                .next(deleteFilesStep)
                .build();
        
    }
    
    /**
     * サブフローの制御を行う．
     * <p>
     * 特殊例外が送出された場合のサブフローを定義する．
     * </p>
     * 
     * @param registExclusiveErrorStep 特殊例外を処理するステップ
     * @param deleteFilesStep ファイル削除を行うステップ
     * @return 特殊例外が送出された場合のサブフロー
     */
    @Bean(name = NameConstant.EXCLUSIVE_ERROR_FLOW_NAME)
    public Flow exclusiveErrorFlow(Step registExclusiveErrorStep,
            Step deleteFilesStep) {
        
        return new FlowBuilder<Flow>(NameConstant.EXCLUSIVE_ERROR_FLOW_NAME)
                .start(registExclusiveErrorStep)
                .next(deleteFilesStep)
                .build();
        
    }
    
    /**
     * ステップフローの制御を行う．
     * <p>
     * 埋設物情報取得処理について、フロー制御を定義する．
     * </p>
     * 
     * @param jobRepository {@link JobRepository}
     * @param tasklet 埋設物情報取得処理を定義したタスクレット
     * @param transactionManager {@link PlatformTransactionManager}
     * @param exceptionListener 例外制御を定義したリスナー
     * @param loggingListener ロギングを行うリスナー
     * @return {@link Step}
     */
    @Bean(name = NameConstant.GET_UF_STEP_NAME)
    public Step getUndergroundFacilityStep(JobRepository jobRepository,
            GetUndergroundFacilityTasklet tasklet,
            PlatformTransactionManager transactionManager,
            StepExecutionListener exceptionListener,
            StepExecutionListener loggingListener) {
        
        return new StepBuilder(NameConstant.GET_UF_STEP_NAME, jobRepository)
                .tasklet(tasklet, transactionManager)
                .listener(exceptionListener)
                .listener(loggingListener)
                .build();
        
    }
    
    /**
     * ステップフローの制御を行う．
     * <p>
     * 普通例外の処理について、フロー制御を定義する．
     * </p>
     * 
     * @param jobRepository {@link JobRepository}
     * @param tasklet 普通例外処理を定義したタスクレット
     * @param transactionManager {@link PlatformTransactionManager}
     * @param loggingListener ロギングを行うリスナー
     * @return {@link Step}
     */
    @Bean(name = NameConstant.REGIST_GENERIC_ERROR_STEP_NAME)
    public Step registGenericErrorStep(JobRepository jobRepository,
            RegistGenericErrorTasklet tasklet,
            PlatformTransactionManager transactionManager,
            StepExecutionListener loggingListener) {
        
        return new StepBuilder(NameConstant.REGIST_GENERIC_ERROR_STEP_NAME, jobRepository)
                .tasklet(tasklet, transactionManager)
                .listener(loggingListener)
                .build();
        
    }
    
    /**
     * ステップフローの制御を行う．
     * <p>
     * 特殊例外の処理について、フロー制御を行う．<br>
     * ここで処理される特殊例外として、下記の例外を想定している．<br>
     * ・データベース更新中<br>
     * ・データ整備範囲外
     * </p>
     * 
     * @param jobRepository {@link JobRepository}
     * @param tasklet 特殊例外処理を定義したタスクレット
     * @param transactionManager {@link PlatformTransactionManager}
     * @param loggingListener ロギングを行うリスナー
     * @return {@link Step}
     */
    @Bean(name = NameConstant.REGIST_EXCLUSIVE_ERROR_STEP_NAME)
    public Step registExclusiveErrorStep(JobRepository jobRepository,
            RegistExclusiveErrorTasklet tasklet,
            PlatformTransactionManager transactionManager,
            StepExecutionListener loggingListener) {
        
        return new StepBuilder(NameConstant.REGIST_EXCLUSIVE_ERROR_STEP_NAME, jobRepository)
                .tasklet(tasklet, transactionManager)
                .listener(loggingListener)
                .build();
        
    }
    
    /**
     * ステップフローの制御を行う．
     * <p>
     * ファイル削除の処理について、フロー制御を行う．
     * </p>
     * 
     * @param jobRepository {@link JobRepository}
     * @param tasklet ファイル削除処理を定義したタスクレット
     * @param transactionManager {@link PlatformTransactionManager}
     * @param loggingListener ロギングを行うリスナー
     * @return {@link Step}
     */
    @Bean(name = NameConstant.DELETE_FILES_STEP_NAME)
    public Step deleteFilesStep(JobRepository jobRepository,
            DeleteFilesTasklet tasklet,
            PlatformTransactionManager transactionManager,
            StepExecutionListener loggingListener) {
        
        return new StepBuilder(NameConstant.DELETE_FILES_STEP_NAME, jobRepository)
                .tasklet(tasklet, transactionManager)
                .listener(loggingListener)
                .build();
        
    }
    
}
