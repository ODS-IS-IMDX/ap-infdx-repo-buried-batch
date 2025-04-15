// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import com.spatialid.app.common.constants.BatchCommonConstant;

/**
 * 空間・属性情報参照の呼び出しにおける、リトライを行う例外クラス．<br>
 * 特に参照先データが更新でロックされていた場合に送出される．<br>
 * 特殊例外として扱う．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
public class RetryableSidAttributeLockedException extends RetryableException {
    
    private static final long serialVersionUID = 1L;
    
    public RetryableSidAttributeLockedException(String errorDetailInRegist) {
        
        super(BatchCommonConstant.MSG_DATA_LOCKED_ERROR, errorDetailInRegist);
        
    }
    
}
