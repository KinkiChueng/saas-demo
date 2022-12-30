package com.example.saas2.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.saas2.dto.DataSourceDto;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjinqi
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static Map<Object, Object> dataSourceMap = new HashMap<>();
    private static DynamicDataSource instance;
    private static byte[] lock=new byte[0];

    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * 如果不希望数据源在启动配置时就加载好，可以定制这个方法，从任何你希望的地方读取并返回数据源
     * 比如从数据库、文件、外部接口等读取数据源信息，并最终返回一个DataSource实现类对象即可
     */
    @Override
    protected DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }
    /**
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据，定制这个方法
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    /**
     * 判断数据源是否已经添加 动态数据源注入时判断调用，兼容数据源更新，防填
     * @param dataSourceDto
     * @return
     */
    public Boolean existDatasource(DataSourceDto dataSourceDto, String url) {
        if (dataSourceMap.containsKey(dataSourceDto.getCode())) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSourceMap.get(dataSourceDto.getCode());
            if (druidDataSource.getUrl().equals(url) && druidDataSource.getUsername().equals(dataSourceDto.getUser()) &&
                    druidDataSource.getPassword().equals(dataSourceDto.getPassword())) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 判断数据源是否已经添加
     */
    public Boolean existDatasource(String dataSourceName) {
        if (dataSourceMap.containsKey(dataSourceName)) {
            return true;
        }
        return false;
    }

    /**
     * 判断数据源是否已经添加
     */
    public Boolean existDatasource(String dataSourceName,DataSourceDto dataSourceDto, String url) {
        if (dataSourceMap.containsKey(dataSourceName)) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSourceMap.get(dataSourceName);
            if (druidDataSource.getUrl().equals(url) && druidDataSource.getUsername().equals(dataSourceDto.getUser()) &&
                    druidDataSource.getPassword().equals(dataSourceDto.getPassword())) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 设置默认数据源
     * @param defaultDataSource
     */
    public void setDefaultDataSource(Object defaultDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
    }

    /**
     * 设置数据源
     * @param targetDataSources
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        // 将数据源的 key 放到数据源上下文的 key 集合中，用于切换时判断数据源是否有效
        DynamicDataSourceContextHolder.addDataSourceKeys(targetDataSources.keySet());
        super.afterPropertiesSet();// 必须添加该句，否则新添加数据源无法识别到
    }

    public static synchronized DynamicDataSource getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance=new DynamicDataSource();
                }
            }
        }
        return instance;
    }
}
