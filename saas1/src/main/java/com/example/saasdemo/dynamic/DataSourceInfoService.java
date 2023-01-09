package com.example.saasdemo.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.saasdemo.dto.DataSourceDto;
import com.gwmfc.util.DatabaseEnum;
import com.gwmfc.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.gwmfc.util.Const.*;
import static com.gwmfc.util.DatabaseEnum.*;

/**
 * @author zhangjinqi
 */
@Service
@Slf4j
public class DataSourceInfoService {
    public static final String DEFAULT = "default";

    /**
     * 切换数据源，如果不存在就添加
     * 如果是通过任务创建的数据源不存在分库分表问题 同时也不用创建trace 当做普通数据源注入即可
     * 源数据解析中存在分库分表系统，需要把所有数据库注入系统 页面用逗号分隔传入
     * @param dataSourceDto
     *                任务 元数据分析 分库分表
     *            mysql	√	  √	        √
     *           oracle	×	  √	        ×
     *        sqlserver	×	  √	        ×
     *       postgresql	×	  √	        ×
     *
     */
    public void setDataSourceInfo(DataSourceDto dataSourceDto) {
        DynamicDataSourceContextHolder.setDataSourceKey(DEFAULT);
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();

        log.info("dataSourceDto   code:{},type:{},dbname:{}",dataSourceDto.getCode(),dataSourceDto.getType(),dataSourceDto.getDbName());
        String url = getUrlToDruid(dataSourceDto);
        Map<Object, Object> dataSourceMap = new HashMap<>(16);
            if (!dynamicDataSource.existDatasource(dataSourceDto, url)) {
                DruidDataSource druidDataSource = new DruidDataSource();
                druidDataSource.setName(dataSourceDto.getCode());
                druidDataSource.setDriverClassName(DatabaseEnum.getCodeEnum(dataSourceDto.getType()));
                druidDataSource.setUrl(url);
                druidDataSource.setUsername(dataSourceDto.getUser());
                druidDataSource.setPassword(dataSourceDto.getPassword());
                druidDataSource.setKeepAlive(true);
                druidDataSource.setBreakAfterAcquireFailure(true);
                druidDataSource.setConnectionErrorRetryAttempts(10);
                dataSourceMap.put(dataSourceDto.getCode(), druidDataSource);

                Map<Object, Object> map = dynamicDataSource.getDataSourceMap();
                map.putAll(dataSourceMap);
                dynamicDataSource.setTargetDataSources(map);

            }
    }

    private String getUrlToDruid(DataSourceDto dataSourceDto) {
        switch (dataSourceDto.getType()) {
            case MYSQL:
                return MySQL.getUrlHeader() + dataSourceDto.getIp() + ":" + dataSourceDto.getPort() + "/" + dataSourceDto.getDbName();
            case SQLSERVER:
                return SqlServer.getUrlHeader() + dataSourceDto.getIp() + ":" + dataSourceDto.getPort() + ";DatabaseName=" + dataSourceDto.getDbName();
            case ORACLE:
                return Oracle.getUrlHeader() + dataSourceDto.getIp() + ":" + dataSourceDto.getPort() + "/" + dataSourceDto.getDbName();
            case POSTGRESQL:
                return Postgresql.getUrlHeader() + dataSourceDto.getIp() + ":" + dataSourceDto.getPort() + "/" + dataSourceDto.getDbName();
            default:
                return "";
        }
    }
}
