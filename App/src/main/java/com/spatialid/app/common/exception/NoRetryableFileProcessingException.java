// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

/**
 * ファイル処理における、リトライを行わない例外クラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/10/03
 */
public class NoRetryableFileProcessingException extends NoRetryableException {

    private static final long serialVersionUID = 1L;
    
    public NoRetryableFileProcessingException(String errorDetailInLog) {
        
        super(errorDetailInLog, "");
        
    }
    
}
