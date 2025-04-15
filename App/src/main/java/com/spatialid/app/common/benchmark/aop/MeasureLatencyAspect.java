// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.benchmark.aop;

import java.text.DecimalFormat;
import java.util.Locale;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.spatialid.app.common.benchmark.annotation.MeasureLatency;
import com.spatialid.app.common.constants.BatchCommonConstant;

import lombok.extern.log4j.Log4j2;

/**
 * {@link MeasureLatency}が付与されているメソッドに対して、処理時間計測を行うクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/10/03
 */
@Aspect
@Component
@Log4j2
public class MeasureLatencyAspect {
    
    /**
     * メッセージプロパティ．
     */
    private MessageSource messageSource;
    
    public MeasureLatencyAspect(@Qualifier("reloadableResourceBundleMessageSource") MessageSource messageSource) {
        
        this.messageSource = messageSource;
        
    }
    
    /**
     * {@link MeasureLatency}が付与されたメソッドに対して、処理時間の計測を行う．
     * <p>
     * 対象メソッドの処理時間・メソッド名を取得してロギングを行う．<br>
     * ログはINFOとして出力される．<br>
     * 計測値はミリ秒 かつ 小数点1桁までで切り捨てを行う．
     * </p>
     * 
     * @param joinPoint {@link ProceedingJoinPoint} 対象メソッドの情報を保持、または操作するオブジェクト
     * @return 対象メソッドの返却値
     * @throws Throwable
     */
    @Around("@annotation(com.spatialid.app.common.benchmark.annotation.MeasureLatency)")
    public Object measureLatency(ProceedingJoinPoint joinPoint) throws Throwable {
        
        final long starTime = System.nanoTime();
        
        final Object result = joinPoint.proceed();
        
        final long endTime = System.nanoTime();
        
        final double duration = (endTime - starTime) / 1000000.0;
        
        // 小数点以下1桁まで切り捨て
        final DecimalFormat fm = new DecimalFormat("0.0");
        
        final String latency = fm.format(duration);
        
        final String targetName = joinPoint.getSignature().getName();
        
        log.info(messageSource.getMessage(BatchCommonConstant.KEY_LOGGING_MSG_MEASURE, new String[] {targetName, latency}, Locale.JAPAN));
        
        return result;
        
    }
    
}
