package com.example.saasdemo.service;

import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.dao.datasource.DataSourceMapper;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import com.gwmfc.domain.ConfigDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangjinqi
 */
@Service
public class ConfigService {
    @Resource
    private DataSourceMapper dataSourceMapper;

    @FixValue(propertyName = "${nacos.tenant}")
    private String tenantId;

    public void updateConfigValue(ConfigDto configDto) {
        DynamicDataSourceContextHolder.setDataSourceKey("default");
        dataSourceMapper.updateConfigValue(configDto);
    }

    public String getTenantId() {
       return tenantId;
    }
}
