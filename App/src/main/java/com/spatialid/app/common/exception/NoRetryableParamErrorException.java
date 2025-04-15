// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * バッチのパラメータチェックにおける、リトライを行わないクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/11/01
 */
public class NoRetryableParamErrorException extends NoRetryableException {
    
    private static final long serialVersionUID = 1L;

    public NoRetryableParamErrorException(Map<String, String> violationMap) {
        
        super(createErrorDetailInLog(violationMap), "");
        
    }
        
    /**
     * バリデーション違反となったフィールド名・値のマップを元に、ログメッセージを生成する．<br>
     * スーパークラスのコンストラクタの引数に使用するため、staticメソッドとする．
     * 
     * @param violationMap バリデーション違反となったフィールド名・値のマップ
     * @return バリデーション違反時のエラーメッセージ
     */
    private static String createErrorDetailInLog(Map<String, String> violationMap) {
        
        // 違反フィールド名をカンマで接続
        final String violationKeys = violationMap.keySet()
                .stream()
                .collect(Collectors.joining(","));
        
        // 違反フィールド名と値を=で接続したものをカンマで接続
        final String violationFields = violationMap.entrySet()
                .stream()
                .map(entry -> {
                    
                    final StringBuilder keysBuilder = new StringBuilder();
                    
                    return keysBuilder.append(entry.getKey())
                            .append("=")
                            .append(entry.getValue())
                            .toString();
                    
                })
                .collect(Collectors.joining(","));
        
        final StringBuilder msgBuilder = new StringBuilder();
        
        return msgBuilder.append("パラメータ：")
                .append(violationKeys)
                .append("が正しくありません。")
                .append("[")
                .append(violationFields)
                .append("]")
                .toString();
        
    }
    
}
