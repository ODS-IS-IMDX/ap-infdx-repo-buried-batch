// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import com.spatialid.app.common.constants.BatchCommonConstant;

/**
 * 空間・属性情報参照の呼び出しにおける、リトライを行う例外クラス．<br>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
public class RetryableSidAttributeException extends RetryableException {
    
    private static final long serialVersionUID = 1L;
        
    public RetryableSidAttributeException(String errorDetailInLog) {
        
        super(errorDetailInLog, BatchCommonConstant.MSG_ERROR);
        
    }
    
}
