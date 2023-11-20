package com.config.database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DynamicDataSource extends AbstractRoutingDataSource {
        private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

        @Autowired
        private DataSourceRegisterNew dataSourceRegister;

        @Override
        protected Object determineCurrentLookupKey() {
            String tenant = DataSourceTenantContextHolder.getCurrentTenant();
            logger.info("== Current datasource {}.", tenant);
            return tenant;
        }

        @Override
        public void afterPropertiesSet() {
            super.setTargetDataSources(dataSourceRegister.getDataSourceMap());
            //super.setDefaultTargetDataSource(dataSourceRegister.getDefaultDataSource());
            //设置默认数据源
            //logger.info("Set default datasource to [{}].", dataSourceRegister.getDefaultDataSourceName());
            //对Map目标数据源进行解析，形成最终使用的 resolvedDataSources
            super.afterPropertiesSet();
        }

        @Override
        public Connection getConnection() throws SQLException {
            return super.getConnection();
        }

        /*public DataSource getDataSource(String dataSourceName) {
            return dataSourceRegister.getDataSourceByName(dataSourceName);
        }*/

        /*public String getDefaultDataSourceName() {
            return dataSourceRegister.getDefaultDataSourceName();
        }

        public boolean containsDataSource(String dataSourceKey) {
            return dataSourceRegister.containsDataSource(dataSourceKey);
        }*/
    }