package com.config.database;


import com.frame.core.datasource.ConfigFileLocation;
import com.yunhe.zzedu.web.config.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description：加载数据源及连接池的配置文件
 * @autor：zhuzhenhong
 * @date :5/28/20
 */
@Component
@DependsOn("applicationContextUtil")
public class DataSourcePropertyLoader {
    private static final Logger logger = LoggerFactory.getLogger(DataSourcePropertyLoader.class);
    private ConfigurableEnvironment configurableEnvironment;
    private static final String DS_RESOURCE_NAME = "datasource";
    private static final String DS_COMMON_RESOURCE_NAME = "datasourceCommon";
    private static final String DEFAULT_DATASOURCE_KEY = "default.datasource";
    private static final String PACKAGE_ROUTER = "package.router";

    @PostConstruct
    public void initPropertyLoader() {
        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        this.configurableEnvironment = (ConfigurableEnvironment) applicationContext.getEnvironment();
    }

    /**
     * 加载数据源及连接池配置文件
     *
     * @throws IOException
     */
    public void loadDataSourceProperties() throws IOException {
        Resource dsResource = new ClassPathResource(ConfigFileLocation.DS_CONFIG_PATH);
        Resource jdbcPoolResource = new ClassPathResource(ConfigFileLocation.JDBC_POOL_PATH);
        Resource packageRouterResource = new ClassPathResource(ConfigFileLocation.PACKAGE_ROUTER_PATH);
        if (dsResource.exists()) {
            PropertySource<?> propertySource = new ResourcePropertySource(DS_RESOURCE_NAME, dsResource);
            configurableEnvironment.getPropertySources().addLast(propertySource);
            logger.info("{} is loaded!", ConfigFileLocation.DS_CONFIG_PATH);
        } else {
            logger.error("Haven't found [{}].", ConfigFileLocation.DS_CONFIG_PATH);
        }
        if (jdbcPoolResource.exists()) {
            PropertySource<?> propertySource = new ResourcePropertySource(DS_COMMON_RESOURCE_NAME, jdbcPoolResource);
            configurableEnvironment.getPropertySources().addLast(propertySource);
            logger.info("{} is loaded!", ConfigFileLocation.JDBC_POOL_PATH);
        } else {
            logger.error("Haven't found [{}].", ConfigFileLocation.JDBC_POOL_PATH);
        }
        if (packageRouterResource.exists()) {
            PropertySource<?> propertySource = new ResourcePropertySource(PACKAGE_ROUTER, packageRouterResource);
            configurableEnvironment.getPropertySources().addLast(propertySource);
            logger.info("{} is loaded!", ConfigFileLocation.PACKAGE_ROUTER_PATH);
        } else {
            logger.error("Haven't found [{}].", ConfigFileLocation.PACKAGE_ROUTER_PATH);
        }
    }

    /**
     * 返回datasource.property文件所有数据源名称前缀标识
     *
     * @return
     * @throws IOException
     */
    public Set<String> getAllDataSourceNames() throws IOException {
        loadDataSourceProperties();
        Set<String> dataSourceNameSet = new HashSet<>();
        MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        for (PropertySource<?> propertySource : propertySources) {
            if (propertySource instanceof EnumerablePropertySource<?>) {
                String[] propertyNames = ((EnumerablePropertySource) propertySource).getPropertyNames();
                if (propertySource.getName().equals(DS_RESOURCE_NAME)) {
                    for (String propertyName : propertyNames) {
                        //默认数据源配置
                        if (propertyName.equals(DEFAULT_DATASOURCE_KEY)) {
                            continue;
                        }
                        Pattern pattern = Pattern.compile("^(?<dataSourceName>.*)\\.");
                        Matcher matcher = pattern.matcher(propertyName);
                        if (matcher.find()) {
                            dataSourceNameSet.add(matcher.group("dataSourceName"));
                        }
                    }
                }
            }
        }
        return dataSourceNameSet;
    }

    /**
     * 获取默认数据源名称
     *
     * @return
     * @throws Exception
     */
    public String getDefaultDataSourceName() {
        String defaultDataSourceLookupKey = configurableEnvironment.getProperty(DEFAULT_DATASOURCE_KEY, DS_RESOURCE_NAME).trim();
        return defaultDataSourceLookupKey;
    }

    /**
     * 获取按包路径数据源路由配置信息
     *
     * @return
     */
    public Map<String, String> getPackageRouterCfg() {
        PropertySource propertySource = configurableEnvironment.getPropertySources().get(PACKAGE_ROUTER);
        if (propertySource == null) {
            logger.error("The PropertySource of package.router is null,please check the [{}] exists!", ConfigFileLocation.PACKAGE_ROUTER_PATH);
            return null;
        }
        Map<String, String> pkgRouter = new HashMap<>();
        String[] propertyNames = ((EnumerablePropertySource) propertySource).getPropertyNames();
        for (String propertyName : propertyNames) {
            if (StringUtils.isNotEmpty(propertyName)) {
                Object val = propertySource.getProperty(propertyName);
                if (val != null) {
                    pkgRouter.put(propertyName, (String) val);
                }
            }
        }
        return pkgRouter;
    }
}

