// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.spatialid.app.listener.ExitCodeListener;

/**
 * SpringBootのエントリーポイントを定義したクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/17
 */
@SpringBootApplication
public class InfraBatchIndx0010Application {
        
    /**
     * メインクラス．
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        ConfigurableApplicationContext context = SpringApplication.run(InfraBatchIndx0010Application.class, args);
        
        System.exit(SpringApplication.exit(context, () -> context.getBean(ExitCodeListener.class).getExitCode()));
        
    }
    
}
