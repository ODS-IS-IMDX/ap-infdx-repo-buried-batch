// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * api.propertiesの値を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/17
 */
@Component
@ConfigurationProperties(prefix = "api.common")
@Data
public class ApiProperty {
    
    /**
     * 接続待機時間．
     */
    private int connectionTimeout;
    
    /**
     * レスポンス待機時間．
     */
    private int readTimeout;
    
    /**
     * 通信プロトコル．
     */
    private String protocol;
    
    /**
     * 通信ポート．
     */
    private String port;
    
    /**
     * 設備データ出力タスクAPIのパス．
     */
    private String outputtasksPath;
    
    /**
     * パスパラメータを使用する設備データ出力タスクAPIのパス．
     */
    private String putOutputtasksPath;
    
    /**
     * 空間・属性情報参照APIのパス．
     */
    private String sidAttributePath;
    
    /**
     * S3格納APIのパス．
     */
    private String s3Path;
    
}
