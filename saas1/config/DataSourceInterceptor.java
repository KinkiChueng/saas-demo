package com.config;


import com.yunhe.zzedu.common.tool.SystemConstant;
import com.yunhe.zzedu.web.config.database.DataSourceTenantContextHolder;
import com.yunhe.zzedu.web.config.mongo.MongoContext;
import com.yunhe.zzedu.web.config.redis.RedisContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataSourceInterceptor implements HandlerInterceptor {
    private static final InheritableThreadLocal<String> CURRENT_DATASOURCE_KEY = new InheritableThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(DataSourceInterceptor.class);

    public static String getCurrentDataSourceKey(){
        return CURRENT_DATASOURCE_KEY.get();
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantHeader = request.getHeader("tenant");
        logger.info("current  datasource key [{}].", tenantHeader);
        CURRENT_DATASOURCE_KEY.set(tenantHeader);

        String dbTenant = SystemConstant.ALL_TENANT_DATABASES_NAME.get(tenantHeader);
        logger.info("current  datasource to [{}].", dbTenant);
        DataSourceTenantContextHolder.setCurrentTanent(dbTenant);

        String MongoTenant = SystemConstant.ALL_TENANT_MONGO_NAME.get(tenantHeader);
        logger.info("current  mongo to [{}].", MongoTenant);
        MongoContext.setMongoDbFactory(MongoTenant);

        Integer redisTenant = SystemConstant.ALL_TENANT_REDIS_NAME.get(tenantHeader);
        logger.info("current  redis to [{}].", redisTenant);
        RedisContext.select(redisTenant);
        return true;
    }
}

