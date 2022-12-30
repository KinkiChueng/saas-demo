package com.example.saasdemo.controller;

import com.example.saasdemo.api.MetadataFeignApi;
import com.example.saasdemo.api.SaasFeignApi;
import com.example.saasdemo.config.CurrentUser;
import com.example.saasdemo.dao.datasource.DataSourceMapper;
import com.example.saasdemo.dto.DataSourceDto;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import com.gwmfc.domain.User;
import com.gwmfc.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/nacos")
@RefreshScope
public class nacosController {
    @Resource
    private SaasFeignApi saasFeignApi;

    @Resource
    private MetadataFeignApi metadataFeignApi;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @GetMapping("/getTenantId")
    String selectConfigValue() {
        return dataSourceMapper.selectConfigValue("nacos.tenant");
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
    Result getMySqlInfo() {
        return Result.ok(dataSourceMapper.selectInfo());
    }

    @GetMapping("/getFeignInfo")
    String getFeignInfo() {
        return (String) saasFeignApi.answerFeignInfo().getData();
    }
}
