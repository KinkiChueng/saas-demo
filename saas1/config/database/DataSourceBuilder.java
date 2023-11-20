package com.config.database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.core.env.PropertyResolver;

import java.lang.reflect.Method;

public class DataSourceBuilder {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceBuilder.class);
    private String datasourceCommonPrefix = "pool";
    private String dataSourcePrefix;
    private BasicDataSource dataSource;
    private final PropertyResolver environment;
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    static {
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }

    private enum ConfigField {
        INITIAL_SIZE("initialSize", int.class),
        MIN_IDLE("minIdle", int.class),
        Max_TOTAL("maxTotal", int.class),
        MAX_WAIT_MILLIS("maxWaitMillis", long.class),
        TIME_BETWEEN_EVICTION_RUNS_MILLIS("timeBetweenEvictionRunsMillis", long.class),
        MIN_EVICTABLE_IDLE_TIME_MILLIS("minEvictableIdleTimeMillis", long.class),
        VALIDATION_QUERY("validationQuery", String.class),
        TEST_WHILE_IDLE("testWhileIdle", boolean.class),
        TEST_ON_RETURN("testOnReturn", boolean.class),
        DRIVER_CLASS_NAME("driverClassName", String.class),
        URL("url", String.class),
        USER_NAME("username", String.class),
        PASSWORD("password", String.class);


        private String fieldName;
        private Class<?> typeClass;

        ConfigField(String fieldName, Class<?> typeClass) {
            this.fieldName = fieldName;
            this.typeClass = typeClass;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class<?> getTypeClass() {
            return typeClass;
        }
    }

    public DataSourceBuilder(PropertyResolver environment, String prefix) {
        this.environment = environment;
        this.dataSourcePrefix = prefix;
    }

    public BasicDataSource createDataSource() throws Exception {
        if (dataSource == null) {
            this.dataSource = new BasicDataSource();
            logger.info("The loaded datasource configuration information is as follows:");
            for (ConfigField configField : ConfigField.values()) {
                String dsPropName = dataSourcePrefix + "." + configField.getFieldName();
                String jdbcPoolPropName = datasourceCommonPrefix + "." + configField.getFieldName();
                //把property的value值转换成具体的类型
                Object dsValue = environment.getProperty(dsPropName, configField.getTypeClass());
                Object poolValue = environment.getProperty(jdbcPoolPropName, configField.getTypeClass());
                if (dsValue != null) {
                    if ("password".equals(configField.getFieldName())) {
                        //TODO 解密处理

                    }
                    setDataSourceProperty(dataSource, configField, dsValue);
                    logger.info(configField.getFieldName() + "=" + dsValue);
                } else {
                    if (poolValue != null) {
                        setDataSourceProperty(dataSource, configField, poolValue);
                        logger.info(configField.getFieldName() + "=" + poolValue);
                    }
                }
            }
        }
        return dataSource;
    }

    private void setDataSourceProperty(BasicDataSource dataSource, ConfigField configField, Object propertyValue) throws Exception {
        try {
            Method method = dataSource.getClass().getMethod(resolveSetter(configField.getFieldName()), configField.getTypeClass());
            method.invoke(dataSource, propertyValue);
        } catch (Exception e) {
            throw new Exception("Initial DataSource object failed!", e);
        }
    }

    private String resolveSetter(String fieldName) {
        return new StringBuilder("set").append(Character.toUpperCase(fieldName.charAt(0)))
                .append(fieldName.substring(1))
                .toString();
    }

}

