// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spatialid.app.common.properties.AwsProperty;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

/**
 * AWSのクライアントをカスタマイズするクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/30
 */
@Configuration
public class AwsConfig {
    
    /**
     * AWSの情報を保持するクラス．
     */
    private final AwsProperty awsProperty;
        
    public AwsConfig(AwsProperty awsProperty) {
        
        this.awsProperty = awsProperty;
        
    }
    
    /**
     * {@link SecretsManagerClient}にリージョンを設定してBeanに登録する．
     * 
     * @return {@link SecretsManagerClient}
     */
    @Bean
    public SecretsManagerClient secretsManagerClient() {
        
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.of(awsProperty.getRegion()))
                .build();
        
        return secretsManagerClient;
        
    }
    
}
