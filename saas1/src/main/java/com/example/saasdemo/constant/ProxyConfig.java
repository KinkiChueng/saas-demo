package com.example.saasdemo.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author by jay
 * @Classname ProxyConfig
 * @Description ProxyConfig描述
 * @Date 2021/07/26
 */
//@Component
//@ConfigurationProperties(prefix = "proxy")
@Data
public class ProxyConfig {
    /**
     * is proxy enabled
     */
    private Boolean enabled;
    /**
     * proxy host address
     */
    private String host;
    /**
     * proxy port address
     */
    private Integer port;
}
