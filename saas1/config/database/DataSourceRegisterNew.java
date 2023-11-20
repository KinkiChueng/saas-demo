package com.config.database;

import com.yunhe.zzedu.web.config.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 多数据源注册服务类
 */
@Component
public class DataSourceRegisterNew {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceRegisterNew.class);
    @Autowired
    private DataSourcePropertyLoader dataSourcePropertyLoader;

    private Map<Object, Object> dataSourceMap = new HashMap<>();

    //private String defaultDataSourceName;

    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    /*public String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }

    public void setDefaultDataSourceName(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
    }*/

    /**
     * 根据config/datasource.properties注册所有数据源
     *
     * @throws Exception
     */
    @PostConstruct
    public void registerAllDataSource() throws Exception {
        Set<String> dataSourceNameSet = dataSourcePropertyLoader.getAllDataSourceNames();
        for (String dataSourceName : dataSourceNameSet) {
            DataSourceBuilder dataSourceBuilder = new DataSourceBuilder(ApplicationContextUtil.getApplicationContext().getEnvironment(), dataSourceName);
            dataSourceMap.put(dataSourceName, dataSourceBuilder.createDataSource());
            logger.info("Register datasource [{}] successfully.", dataSourceName);
        }
        //setDefaultDataSourceName(dataSourcePropertyLoader.getDefaultDataSourceName());
    }

    /*public Object getDefaultDataSource() {
        return dataSourceMap.get(defaultDataSourceName);
    }*/

    /**
     * 根据数据源名称获取数据源实例对象
     *
     * @param dataSourceName
     * @return
     */
    /*public DataSource getDataSourceByName(String dataSourceName) {
        DataSource dataSource = null;
        if (dataSourceMap != null) {
            Object obj = dataSourceMap.get(dataSourceName);
            if (obj != null) {
                dataSource = (DataSource) dataSourceMap.get(dataSourceName);
            } else {
                logger.error("Can not get datasource[{}] from dataSourceMap.", dataSourceName);
            }
        }
        return dataSource;
    }*/

    /**
     * 判断指定dataSource是否存在
     *
     * @param dataSourceKey
     * @return
     */
    /*public boolean containsDataSource(String dataSourceKey) {
        return dataSourceMap.containsKey(dataSourceKey);
    }*/
}

