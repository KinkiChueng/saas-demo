package com.example.saasdemo.service;

import com.example.saasdemo.dao.datasource.DataSourceMapper;
import com.example.saasdemo.dto.ConfigDto;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangjinqi
 */
@Service
public class ConfigService {
    @Resource
    private DataSourceMapper dataSourceMapper;

    public void updateConfigValue(ConfigDto configDto) {
        DynamicDataSourceContextHolder.setDataSourceKey("default");
        dataSourceMapper.updateConfigValue(configDto);
    }
}
