package com.example.saasdemo.controller;

import com.example.saasdemo.api.MetadataFeignApi;
import com.example.saasdemo.api.SaasFeignApi;
import com.example.saasdemo.custom.annotation.CurrentUser;
import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.dao.datasource.DataSourceMapper;
import com.example.saasdemo.dto.DataSourceDto;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import com.gwmfc.domain.User;
import com.gwmfc.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RefreshScope
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@RequestMapping("/nacos")
public class NacosController {
    @Resource
    private SaasFeignApi saasFeignApi;

    @Resource
    private MetadataFeignApi metadataFeignApi;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @FixValue(propertyName = "${nacos.tenant}")
    private String tenantId;

    @GetMapping("/getTenantId")
    String selectConfigValue() {
        return dataSourceMapper.selectConfigValue("nacos.tenant");
    }

    @GetMapping("/getTenantIdByFixValue")
    String selectConfigValueByFixValue() {
        return tenantId;
    }

    @GetMapping("/getTenantIdDefault")
    String selectConfigValueFromDefault(@CurrentUser User user) {
        DynamicDataSourceContextHolder.setDataSourceKey("default");
        String config = dataSourceMapper.selectConfigValue("nacos.tenant");
        DataSourceDto dataSourceDto = metadataFeignApi.getOne(user.getTenantId()).getData();
        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceDto.getCode());
        return config;
    }

    @GetMapping("/getMySqlInfo")
    Result getMySqlInfo() throws InterruptedException {
        Thread.sleep(100000l);
        return Result.ok(dataSourceMapper.selectInfo());
    }

    @GetMapping("/getFeignInfo")
    Result getFeignInfo() {
        return Result.ok(String.valueOf(saasFeignApi.answerFeignInfo().getData()));
    }
}
