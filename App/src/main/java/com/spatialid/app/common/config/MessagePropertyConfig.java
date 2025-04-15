// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import lombok.NoArgsConstructor;

/**
 * ロギングメッセージの読み込みを設定するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/26
 */
@Configuration
@NoArgsConstructor
public class MessagePropertyConfig {
        
    /**
     * logging.propertiesの読み込みを行う．
     * 
     * @return {@link MessageSource}
     */
    @Bean
    MessageSource reloadableResourceBundleMessageSource() {
        
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        messageSource.addBasenames("classpath:logging");
        
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        
        return messageSource;
        
    }
    
}
