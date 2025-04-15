// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto;

import lombok.Data;

/**
 * 各APIのエラーレスポンスを保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/17
 */
@Data
public class ErrorResponse {
        
    /**
     * エラー概要．
     */
    private String code;
    
    /**
     * エラーメッセージ．
     */
    private String message;
    
    /**
     * タイムスタンプ．
     */
    private String detail;
    
}
