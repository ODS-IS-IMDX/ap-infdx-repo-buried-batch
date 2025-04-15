// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.secretsmanager.secretvalue;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * AWS Secrets Managerから取得した値を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/30
 */
@Component
@Data
public class SecretsValue {
        
    /**
     * 業務共通コンテナのドメイン．
     */
    @JsonProperty("ECS-COMMON-DOMAIN")
    private String commonDomain;
    
}
