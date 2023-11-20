package com.example.saasdemo.controller;

import com.example.saasdemo.constant.MappingDataSourceConstant;
import com.example.saasdemo.constant.ProxyConfig;
import com.example.saasdemo.constant.UploadMachineInfoConfig;
import com.example.saasdemo.custom.annotation.FixMappingRules;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjinqi
 * @date 2023年07月11日 16:17
 */
@Slf4j
@RestController
//@RefreshScope
@RequestMapping("/nacos")
public class MappingRuleController {
    @FixMappingRules(prefix = "proxy",profile_name = "saas-service1")
    private ProxyConfig proxyConfig;
    @FixMappingRules(prefix = "machine",profile_name = "saas-service1")
    private UploadMachineInfoConfig uploadMachineInfoConfig;
    @FixMappingRules(prefix = "tenant",profile_name = "multi-tenant-datasource")
    private MappingDataSourceConstant mappingDataSourceConstant;

    @GetMapping("/getMappingDataSourceConstant")
    String getMappingDataSourceConstant() {
        return mappingDataSourceConstant.toString();
    }

    @GetMapping("/getProxyConfig")
    String getProxyConfig() {
        return proxyConfig.toString();
    }

    @GetMapping("/getUploadMachineInfoConfig")
    String getUploadMachineInfoConfig() {
        return uploadMachineInfoConfig.toString();
    }
}
