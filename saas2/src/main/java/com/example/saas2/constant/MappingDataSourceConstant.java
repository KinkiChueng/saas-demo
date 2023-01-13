package com.example.saas2.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author zhangjinqi
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "tenant")
public class MappingDataSourceConstant {
    Map<Long,Map<String, String>> datasource;

    public Map<Long, Map<String, String>> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<Long, Map<String, String>> datasource) {
        this.datasource = datasource;
    }
}
