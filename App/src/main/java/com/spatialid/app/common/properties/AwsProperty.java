// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * aws.propertiesの値を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/29
 */
@Component
@ConfigurationProperties(prefix = "aws.secrets-manager")
@Data
public class AwsProperty {
    
    /**
     * リージョン．
     */
    private String region;
    
    /**
     * シークレット名．
     */
    private String secretName;
    
}
