package com.example.saasdemo.config;

import com.example.saasdemo.util.ApplicationContextConfigUtil;
import com.example.saasdemo.custom.annotation.FixValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.sql.*;

@Configuration
@Slf4j
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        beanFactory = configurableListableBeanFactory;
    }

    @Bean
    public AutowiredAnnotationBeanPostProcessor initAutowiredAnnotationBeanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor() {
            @Override
            public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
                ReflectionUtils.doWithLocalFields(bean.getClass(), field -> {
                    FixValue customValue = field.getAnnotation(FixValue.class);

                    //取出session中的User
                    if (customValue != null) {
                        ReflectionUtils.makeAccessible(field);

                        String propertyName = customValue.propertyName();
                        if (propertyName != null && !"".equals(propertyName)) {
                            if (!propertyName.startsWith("$")) {
                                field.set(bean, propertyName);
                            } else {
                                propertyName = propertyName.substring(propertyName.indexOf("{")+1,propertyName.lastIndexOf("}"));
                                field.set(bean, getTenantConfig(propertyName));
                            }
                        }
                    }
                });

                return pvs;
            }

            private Object getTenantConfig(String propertyName) {
                WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
                // 从 webApplicationContext 中获取  servletContext
                ServletContext servletContext = webApplicationContext.getServletContext();
                Long tenantId = (Long) servletContext.getAttribute("tenantId");
                if (null == tenantId) {
                    //fixme
                    tenantId = 39L;
                }
                ResultSet resultSet = null;
                Connection conn = null;
                PreparedStatement preparedStatement = null;
                try {
                    TypeConverter typeConverter = new SimpleTypeConverter();
                    String defaultDbDriverName = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue("${spring.datasource.cheetahdb.driverClassName}"), String.class);
                    String defaultDbUrl = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue("${spring.datasource.cheetahdb.url}"), String.class);
                    String defaultDbUser = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue("${spring.datasource.cheetahdb.username}"), String.class);
                    String defaultDdPassword = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue("${spring.datasource.cheetahdb.password}"), String.class);

                    Class.forName(defaultDbDriverName);
                    conn = DriverManager.getConnection(defaultDbUrl, defaultDbUser, defaultDdPassword);
                    String sql = "select `value` from saas_config where property_name = ? and tenant_id = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, propertyName);
                    preparedStatement.setLong(2, tenantId);
                    resultSet = preparedStatement.executeQuery();
                    while(resultSet.next()){
                        return resultSet.getString("value");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    log.error("读取项目配置失败！propertyName：{}\nTenantId:{}\nException:{}", propertyName, tenantId, e);
                } finally {
                    closeDbSource(null, resultSet, preparedStatement, conn);
                }
                return null;
            }

            private void closeDbSource(CallableStatement callableStatement, ResultSet rs, PreparedStatement preStateValue, Connection connection) {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preStateValue != null) {
                    try {
                        preStateValue.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (callableStatement != null) {
                    try {
                        callableStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        autowiredAnnotationBeanPostProcessor.setAutowiredAnnotationType(FixValue.class);
        return autowiredAnnotationBeanPostProcessor;
    }


}