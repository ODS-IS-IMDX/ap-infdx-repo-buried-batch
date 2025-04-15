// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import com.spatialid.app.common.constants.BatchCommonConstant;

/**
 * S3格納の呼び出しにおける、リトライを行う例外クラス．<br>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
public class RetryableS3Exception extends RetryableException {

    private static final long serialVersionUID = 1L;
        
    public RetryableS3Exception(String errorDetailInLog) {
        
        super(errorDetailInLog, BatchCommonConstant.MSG_ERROR);
        
    }
    
    
}
