package com.example.saasdemo.service;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.naming.NacosNamingService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * nacos 服务实例
 * @author zhangjinqi
 * @date 2023年06月30日 14:26
 */
@Configuration
@AllArgsConstructor
public class NacosNamingServiceConfig {

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    @Bean
    public NacosNamingService nacosNamingService() {
        try {
            NacosNamingService service = new NacosNamingService(nacosDiscoveryProperties.getNacosProperties());
            return service;
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }
}