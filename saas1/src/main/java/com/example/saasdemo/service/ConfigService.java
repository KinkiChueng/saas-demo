package com.example.saasdemo.service;

import com.example.saasdemo.dao.datasource.DataSourceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ConfigService {
    @Resource
    private DataSourceMapper dataSourceMapper;

    public List<Map<String, String>> selectConfig(String tenantId, String configName) {
        return dataSourceMapper.selectConfig(tenantId,configName);
    }
}
