package com.example.saasdemo.controller;

import com.example.saasdemo.api.MetadataFeignApi;
import com.example.saasdemo.api.SaasFeignApi;
import com.example.saasdemo.custom.annotation.CurrentUser;
import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.dao.datasource.DataSourceMapper;
import com.example.saasdemo.dto.ConfigDto;
import com.example.saasdemo.dto.DataSourceDto;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import com.example.saasdemo.service.ConfigService;
import com.gwmfc.domain.User;
import com.gwmfc.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhangjinqi
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/nacos")
public class NacosController {
    @Resource
    private SaasFeignApi saasFeignApi;

    @Resource
    private MetadataFeignApi metadataFeignApi;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private ConfigService configService;

    @FixValue(propertyName = "${nacos.tenant}")
    private String tenantId;

    @GetMapping("/getTenantId")
    String selectConfigValue() {
        return dataSourceMapper.selectConfigValue("nacos.tenant",65L);
    }

    @GetMapping("/getTenantIdByFixValue")
    String selectConfigValueByFixValue() {
        return tenantId;
    }

    @GetMapping("/getTenantIdDefault")
    String selectConfigValueFromDefault(@CurrentUser User user) {
        DynamicDataSourceContextHolder.setDataSourceKey("default");
        String config = dataSourceMapper.selectConfigValue("nacos.tenant", 65L);
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

    @PostMapping("/updateConfigValue")
    Result updateConfigValue(@RequestBody ConfigDto configDto) {
        configService.updateConfigValue(configDto);

//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.postForObject("http://localhost:9701/nacos/refreshValueScope", configDto, String.class);

        return Result.ok();
    }

    @PostMapping("/refreshValueScope")
    Result refreshValueScope() {
        return Result.ok(tenantId);
    }

    @GetMapping("/deleteConfigValue")
    Result deleteConfigValue(@RequestParam ConfigDto configDto) {
        configService.updateConfigValue(configDto);
        return Result.ok();
    }
}
