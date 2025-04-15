// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.secretsmanager;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * {@link ISecretsManagerSupport}の実装クラス．<br>
 * AWS Secrets Managerへのアクセスを行うクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/30
 */
@Component
public class SecretsManagerSupport implements ISecretsManagerSupport {
    
    /**
     * AWS Secrets Managerのクライアント．
     */
    private final SecretsManagerClient secretsManagerClient;
    
    public SecretsManagerSupport(SecretsManagerClient secretsManagerClient) {
        
        this.secretsManagerClient = secretsManagerClient;
        
    }
    
    /**
     * {@link ISecretsManagerSupport#getSecretValue(String)}の実装を行う．
     * <p>
     * シークレットの取得を実装する．
     * </p>
     * 
     * @param key シークレットに対応するキー名
     * @return キー名に対応するシークレット
     */
    @Override
    public String getSecret(String key) {
                
        final GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(key)
                .build();
        
        final GetSecretValueResponse valueResponse = secretsManagerClient.getSecretValue(valueRequest);
        
        final String secretsValue = valueResponse.secretString();
        
        return secretsValue;
        
    }
    
}
