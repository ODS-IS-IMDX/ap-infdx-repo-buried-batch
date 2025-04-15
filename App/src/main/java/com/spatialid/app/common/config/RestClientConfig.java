// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.spatialid.app.common.properties.ApiProperty;
import com.spatialid.app.common.secretsmanager.secretvalue.SecretsValue;

/**
 * HTTPクライアントの設定を行うクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/18
 */
@Configuration
public class RestClientConfig {
    
    /**
     * シークレットの値を保持するクラス．
     */
    private final SecretsValue secretsValue;
        
    /**
     * APIの情報を保持するクラス．
     */
    private final ApiProperty apiProperty;
    
    public RestClientConfig(SecretsValue secretsValue,
            ApiProperty apiProperty) {
        
        this.secretsValue = secretsValue;
        this.apiProperty = apiProperty;
        
    }
    
    /**
     * RestClientのビルダーをBeanに登録する．
     * <p>
     * 以下の設定を行う．<br>
     *  タイムアウトは5秒<br>
     *  4xx, 5xxのレスポンスを受け取った際に例外を送出しない<br>
     *  日付を分解しない
     * </p>
     * 
     * @param configurer {@link RestClientBuilderConfigurer}
     * @return 設定済みのビルダー
     */
    @Bean
    public RestClient.Builder customRestClient(RestClientBuilderConfigurer configurer) {

        ClientHttpRequestFactorySettings setting = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(apiProperty.getConnectionTimeout()))
                .withReadTimeout(Duration.ofSeconds(apiProperty.getReadTimeout()));
        
        RestClient.Builder builder = RestClient.builder()
                .defaultStatusHandler(
                        status -> true
                        , (request, response) -> {})
                .requestFactory(ClientHttpRequestFactories.get(setting));

        configurer.configure(builder);

        return builder;

    }
    
    /**
     * 共通APIの呼び出しについて基本的な設定を行う．
     * <p>
     * 共通APIの基本URLを設定する．
     * </p>
     * 
     * @param restClientBuilder {@link RestClient.Builder}
     * @return 共通設定を保持したRestClient
     */
    @Bean
    public RestClient commonApiClient(RestClient.Builder restClientBuilder) {
        
        StringBuilder uri = new StringBuilder();
        uri.append(apiProperty.getProtocol())
            .append("://")
            .append(secretsValue.getCommonDomain())
            .append(":")
            .append(apiProperty.getPort());
        
        RestClient restClient = restClientBuilder.baseUrl(uri.toString()).build();

        return restClient;

    }
    
}
