// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.spatialid.app.common.constants.BatchCommonConstant;
import com.spatialid.app.common.exception.NoRetryableException;
import com.spatialid.app.common.exception.RetryableException;

import lombok.NoArgsConstructor;

/**
 * バッチのリトライを定義するクラス．<br>
 * Spring Retryを使用して、詳細を定義する．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
@Configuration
@NoArgsConstructor
public class RetryConfig {
    
    /**
     * リトライを制御する{@link RetryTemplate}にポリシーの適用を行う．
     * 
     * @param exceptionClassifierRetryPolicy {@link ExceptionClassifierRetryPolicy} 本クラスで定義したExceptionClassifierRetryPolicy
     * @param fixedBackOffPolicy {@link FixedBackOffPolicy} 本クラスで定義したFixedBackOffPolicy
     * @return {@link RetryTemplate}
     */
    @Bean
    public RetryTemplate retryTemplate(@Qualifier("exceptionClassifierPolicyInUF") ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy,
            @Qualifier("backoffPolicyInUF") FixedBackOffPolicy fixedBackOffPolicy) {
        
        RetryTemplate retryTemplate = new RetryTemplate();
        
        retryTemplate.setRetryPolicy(exceptionClassifierRetryPolicy);
        
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        
        return retryTemplate;
        
    }
    
    /**
     * 特定の例外に対して適用するリトライ処理を定義する．
     * <p>
     * 下記の通り、リトライ処理を適用する．<br>
     * ・RetryableException: simpleRetryPolicyの設定に基づいたリトライを行う<br>
     * ・NoRetryableException: リトライを行わない
     * </p>
     * 
     * @param simpleRetryPolicy {@link SimpleRetryPolicy} 本クラスで定義したSimpleRetryPolicy
     * @return {@link ExceptionClassifierRetryPolicy}
     */
    @Bean(name = "exceptionClassifierPolicyInUF")
    public ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy(@Qualifier("retryPolicyInUF") SimpleRetryPolicy simpleRetryPolicy) {
        
        ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        
        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<Class<? extends Throwable>, RetryPolicy>();

        policyMap.put(RetryableException.class, simpleRetryPolicy);
        policyMap.put(NoRetryableException.class, new NeverRetryPolicy());
        
        exceptionClassifierRetryPolicy.setPolicyMap(policyMap);
        
        return exceptionClassifierRetryPolicy;
        
    }
    
    /**
     * リトライの最大試行回数を設定した{@link SimpleRetryPolicy}のBean定義を行う．
     * 
     * @return 最大試行回数が設定されたリトライポリシー
     */
    @Bean(name = "retryPolicyInUF")
    public SimpleRetryPolicy simpleRetryPolicy() {
        
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        
        simpleRetryPolicy.setMaxAttempts(BatchCommonConstant.MAX_RETRY_ATTEMPTS);
        
        return simpleRetryPolicy;
        
    }
    
    /**
     * リトライの待機時間を設定した{@link FixedBackOffPolicy}のBean定義を行う．
     * 
     * @return 待機時間が設定されたリトライポリシー
     */
    @Bean(name = "backoffPolicyInUF")
    public FixedBackOffPolicy fixedBackOffPolicy() {
        
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        
        fixedBackOffPolicy.setBackOffPeriod(BatchCommonConstant.BACKOFF_RETRY_ATTEMPTS);
        
        return fixedBackOffPolicy;
        
    }

}
