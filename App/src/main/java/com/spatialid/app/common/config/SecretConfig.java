// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatialid.app.common.properties.AwsProperty;
import com.spatialid.app.common.secretsmanager.ISecretsManagerSupport;
import com.spatialid.app.common.secretsmanager.secretvalue.SecretsValue;

/**
 * AWS Secrets Managerから取得した値をオブジェクトに設定するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/30
 */
@Configuration
public class SecretConfig {
    
    /**
     * AWS Secrets Managerへのアクセスを管理するクラス．
     */
    private final ISecretsManagerSupport secretsManagerSupport;
    
    /**
     * AWSの情報を保持するクラス．
     */
    private final AwsProperty awsProperty;
    
    /**
     * Jsonオブジェクトマッパー．
     */
    private final ObjectMapper objectMapper;
    
    public SecretConfig(ISecretsManagerSupport secretsManagerSupport,
            AwsProperty awsProperty,
            ObjectMapper objectMapper) {
        
        this.secretsManagerSupport = secretsManagerSupport;
        this.awsProperty = awsProperty;
        this.objectMapper = objectMapper;
        
    }
    
    /**
     * シークレットの値を保持するオブジェクトをBeanに登録する．
     * 
     * @return {@link SecretsValue} シークレットの値を保持するオブジェクト
     * @throws JsonMappingException jsonのマッピング処理に失敗した場合
     * @throws JsonProcessingException jsonの処理に失敗した場合
     */
    @Bean
    public SecretsValue secretsValue() throws JsonMappingException, JsonProcessingException {
        
        final String secrets = secretsManagerSupport.getSecret(awsProperty.getSecretName());
        
        final SecretsValue secretsValue = objectMapper.readValue(secrets, SecretsValue.class);
        
        return secretsValue;
        
    }
    
}
