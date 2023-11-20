package com.config.database;


import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

@Configuration
public class TransactionManagerConfig {

    private final String PROPAGATION_NAME_CHANGE = "PROPAGATION_REQUIRED,-Exception";
    private final String PROPAGATION_NAME_SELECT = "PROPAGATION_REQUIRED,-Exception,readOnly";

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Bean(name = "transactionManagerAdvice")
    public TransactionInterceptor transactionInterceptor() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("add*", PROPAGATION_NAME_CHANGE);
        properties.setProperty("save*", PROPAGATION_NAME_CHANGE);
        properties.setProperty("insert*", PROPAGATION_NAME_CHANGE);
        properties.setProperty("update*", PROPAGATION_NAME_CHANGE);
        properties.setProperty("delete*", PROPAGATION_NAME_CHANGE);
        properties.setProperty("get*", PROPAGATION_NAME_SELECT);
        properties.setProperty("find*", PROPAGATION_NAME_SELECT);
        properties.setProperty("query*", PROPAGATION_NAME_SELECT);
        properties.setProperty("load*", PROPAGATION_NAME_SELECT);
        properties.setProperty("select*", PROPAGATION_NAME_SELECT);
        properties.setProperty("*", PROPAGATION_NAME_CHANGE);
        TransactionInterceptor tsi = new TransactionInterceptor(transactionManager, properties);
        return tsi;
    }

    @Bean
    public BeanNameAutoProxyCreator txProxy() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setInterceptorNames("transactionManagerAdvice");
        beanNameAutoProxyCreator.setBeanNames("*Service", "*ServiceImpl");
        beanNameAutoProxyCreator.setProxyTargetClass(true);
        return beanNameAutoProxyCreator;
    }
}

