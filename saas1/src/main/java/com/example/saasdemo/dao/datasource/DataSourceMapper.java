package com.example.saasdemo.dao.datasource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.saasdemo.dto.ConfigDto;
import com.example.saasdemo.dto.DataSourceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yubohai
 * @Classname DataSourceDao
 * @Date 2021/6/22 9:16
 */
@Mapper
public interface DataSourceMapper extends BaseMapper<DataSourceDto> {
    String selectInfo();

    String selectConfigValue(String propertyName, Long tenantId);

    void updateConfigValue(@Param("configDto") ConfigDto configDto);
}
