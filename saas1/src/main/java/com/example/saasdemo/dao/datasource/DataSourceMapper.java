package com.example.saasdemo.dao.datasource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.saasdemo.dto.DataSourceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yubohai
 * @Classname DataSourceDao
 * @Date 2021/6/22 9:16
 */
@Mapper
public interface DataSourceMapper extends BaseMapper<DataSourceDto> {
    String selectInfo();

    String selectConfigValue(String propertyName);

    List<Map<String, String>> selectConfig(String tenantId, String configName);
}
