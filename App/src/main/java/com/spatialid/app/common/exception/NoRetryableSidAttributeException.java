// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import com.spatialid.app.common.constants.BatchCommonConstant;

/**
 * 空間・属性情報参照の呼び出しにおける、リトライを行わない例外クラス．<br>
 * 特に、整備範囲外のデータに対して参照が行われた場合に送出される．
 * 特殊例外として扱う．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
public class NoRetryableSidAttributeException extends NoRetryableException {
    
    private static final long serialVersionUID = 1L;

    public NoRetryableSidAttributeException(String errorDetailInRegist) {
        
        super(BatchCommonConstant.MSG_DATA_RANGE_ERROR, errorDetailInRegist);
        
    }
    
}
