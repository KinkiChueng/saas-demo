package com.example.saasdemo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author yubohai
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "tenant")
public class MappingDataSource {
    Map<Long,Map<String, String>> datasource;

    public Map<Long, Map<String, String>> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<Long, Map<String, String>> datasource) {
        this.datasource = datasource;
    }
}
