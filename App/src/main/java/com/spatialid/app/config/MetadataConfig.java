// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.config;

import javax.sql.DataSource;

import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.NoArgsConstructor;

/**
 * メタデータ管理をインメモリ(H2)で行うための設定クラス．
 * <p>
 * SpringBatchの基幹機能としてメタデータの管理を行うため、明示的にH2を指定する．<br>
 * 2024/09の段階では、メタデータの使用予定はないため、実行毎にメタデータは揮発する．
 * </p>
 * @author matsumoto kentaro
 * @version 1.1 2024/09/17
 */
@Configuration
@NoArgsConstructor
public class MetadataConfig {
    
    /**
     * H2を指定した{@link DataSource}をBeanに登録する．
     * <p>
     * メタデータ管理テーブルのDDLは、Spring BatchのデフォルトDDLを使用する．
     * </p>
     * @return H2が指定された{@link DataSource}
     */
    @Bean(name = "dataSource")
    @BatchDataSource
    public DataSource metaDataSource() {
        
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
        
    }
    
    /**
     * 外部リソースに対してトランザクション管理を行わない{@link PlatformTransactionManager}をBeanに登録する．
     * @return {@link PlatformTransactionManager}
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        
        return new ResourcelessTransactionManager();
        
    }
    
}
