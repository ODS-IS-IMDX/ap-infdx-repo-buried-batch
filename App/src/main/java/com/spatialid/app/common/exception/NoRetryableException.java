// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.exception;

import com.spatialid.app.common.constants.BatchCommonConstant;

import lombok.Getter;

/**
 * リトライを行わないことを示す例外クラス．<br>
 * リトライが不可能な例外はこのクラスを継承して作成する．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
@Getter
public abstract class NoRetryableException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ログに記録するエラー内容．
     */
    private final String errorDetailInLog;
    
    /**
     * 設備データ出力タスク管理テーブルに登録するエラー内容．
     */
    private final String errorDetailInRegist;

    protected NoRetryableException(String errorDetailInLog, String errorDetailInRegist) {
        
        super(BatchCommonConstant.MSG_ERROR);
        
        this.errorDetailInLog = errorDetailInLog;
        
        this.errorDetailInRegist = errorDetailInRegist;
        
    }
        
}
