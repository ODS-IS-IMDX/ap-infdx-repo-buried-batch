// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

/**
 * 設備データ出力タスク参照の呼び出しにおける、リトライを行わない例外クラス．<br>
 * レスポンス件数が0件の正常レスポンスが返却された場合に、送出される．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/10/03
 */
public class NoRetryableOutputTasksException extends NoRetryableException {

    private static final long serialVersionUID = 1L;
    
    public NoRetryableOutputTasksException(String errorDetailInLog) {
        
        super(errorDetailInLog, "");
        
    }
    
}
