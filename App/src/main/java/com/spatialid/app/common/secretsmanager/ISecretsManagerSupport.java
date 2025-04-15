// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.secretsmanager;

/**
 * AWS Secrets Managerへのアクセスを提供するインターフェース．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/30
 */
public interface ISecretsManagerSupport {
    
    /**
     * シークレットの取得を提供する．
     * 
     * @param key シークレットに対応するキー名
     * @return キー名に対応するシークレット
     */
    public String getSecret(String key);
    
}
