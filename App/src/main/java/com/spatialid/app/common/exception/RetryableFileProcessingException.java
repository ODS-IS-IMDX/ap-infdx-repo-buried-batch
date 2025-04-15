// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import com.spatialid.app.common.constants.BatchCommonConstant;

/**
 * ファイル処理における、リトライを行う例外クラス．<br>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
public class RetryableFileProcessingException extends RetryableException {

    private static final long serialVersionUID = 1L;
    
    public RetryableFileProcessingException(String errorDetailInLog) {
        
        super(errorDetailInLog, BatchCommonConstant.MSG_ERROR);
        
    }
    
}
