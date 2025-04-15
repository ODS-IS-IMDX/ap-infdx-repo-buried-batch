// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.benchmark.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メソッドに対して、処理時間計測を行うことを示すマーカーアノテーション．<br>
 * 実際の計測処理はAOPに移譲する．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/10/03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MeasureLatency {

}
