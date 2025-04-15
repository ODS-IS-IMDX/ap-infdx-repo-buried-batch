// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.spatialid.app.common.properties.ApiProperty;
import com.spatialid.app.common.properties.AwsProperty;
import com.spatialid.app.common.properties.BatchProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * プロパティクラスの設定を行うクラス．<br>
 * SPRING_PROFILES_ACTIVEの値から、環境を識別して読み込むプロパティファイルを切り替える．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/24
 */
@Configuration
@PropertySources({
    @PropertySource("classpath:${spring.profiles.active}/batch-${spring.profiles.active}.properties"),
    @PropertySource("classpath:${spring.profiles.active}/api-${spring.profiles.active}.properties"),
    @PropertySource("classpath:${spring.profiles.active}/aws-${spring.profiles.active}.properties")
})
@Data
@NoArgsConstructor
public class PropertyConfig {
        
    /**
     * aws.propertiesの値を保持するクラスをBeanに登録する．
     * 
     * @return AwsProperty プロパティから取得した値を保持するオブジェクト
     */
    @Bean
    public AwsProperty awsProperty() {
        
        return new AwsProperty();
        
    }
        
    /**
     * api.propertiesの値を保持するクラスをBeanに登録する．
     * 
     * @return プロパティから取得した値を保持するオブジェクト
     */
    @Bean
    public ApiProperty apiProperty() {
        
        return new ApiProperty();
        
    }
    
    /**
     * batch.propertiesの値を保持するクラスをBeanに登録する．
     * 
     * @return プロパティから取得した値を保持するオブジェクト
     */
    @Bean
    public BatchProperty batchProperty() {
        
        return new BatchProperty();
        
    }
    
}
