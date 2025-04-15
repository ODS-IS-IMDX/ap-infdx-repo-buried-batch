// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.s3;

import lombok.Data;

/**
 * S3格納APIのレスポンスを保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/23
 */
@Data
public class PostS3ResponseDto {
    
    /**
     * ファイル格納パス．
     */
    private String filePath;
    
}
