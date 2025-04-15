// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

/**
 * 設備データ出力タスク参照・更新の呼び出しにおける、リトライを行う例外クラス．<br>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
public class RetryableOutputTasksException extends RetryableException {

    private static final long serialVersionUID = 1L;
        
    public RetryableOutputTasksException(String errorDetailInLog) {
        
        super(errorDetailInLog, "");
        
    }

}
