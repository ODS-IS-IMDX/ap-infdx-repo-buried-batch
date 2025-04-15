// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * batch.propertiesの値を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
@Component
@ConfigurationProperties(prefix = "batch")
@Data
public class BatchProperty {
    
    /**
     * ファイルの出力先．
     */
    private String exportPath;
    
}
