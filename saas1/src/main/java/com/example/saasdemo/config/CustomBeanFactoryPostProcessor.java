package com.example.saasdemo.config;

import com.example.saasdemo.constant.RefreshUrlConstant;
import com.example.saasdemo.custom.annotation.FixMappingRules;
import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.util.ApplicationContextConfigUtil;
import com.gwmfc.util.GsonUtil;
import lombok.SneakyThrows;
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
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author zhangjinqi
 */
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
            @SneakyThrows
            @Override
            public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
                if (beanName.contains("scopedTarget.") && !beanName.contains("Controller")) {
                    for (Field field : bean.getClass().getSuperclass().getDeclaredFields()) {
                        FixValue fixValue = field.getAnnotation(FixValue.class);
                        FixMappingRules fixMappingRules = field.getAnnotation(FixMappingRules.class);
                        //取出session中的User
                        if (fixValue != null) {
                            ReflectionUtils.makeAccessible(field);

                            String propertyName = fixValue.propertyName();
                            if (propertyName != null && !"".equals(propertyName)) {
                                if (!propertyName.startsWith("$")) {
                                    field.set(bean, propertyName);
                                } else {
                                    propertyName = propertyName.substring(propertyName.indexOf("{") + 1, propertyName.lastIndexOf("}"));
                                    field.set(bean, getTenantConfig(propertyName, field.getType()));
                                }
                            }
                        }
                        //取出session中的User
                        if (fixMappingRules != null) {
                            ReflectionUtils.makeAccessible(field);
                            Object mappingRules = getMappingRules(fixMappingRules.profile_name(), fixMappingRules.prefix(), field.getType());
                            if (mappingRules != null) {
                                field.set(bean, mappingRules);
                            }
                        }
                    }
                } else {
                    ReflectionUtils.doWithLocalFields(bean.getClass(), field -> {
                        FixValue fixValue = field.getAnnotation(FixValue.class);
                        FixMappingRules fixMappingRules = field.getAnnotation(FixMappingRules.class);

                        //取出session中的User
                        if (fixValue != null) {
                            ReflectionUtils.makeAccessible(field);

                            String propertyName = fixValue.propertyName();
                            if (propertyName != null && !"".equals(propertyName)) {
                                if (!propertyName.startsWith("$")) {
                                    field.set(bean, propertyName);
                                } else {
                                    propertyName = propertyName.substring(propertyName.indexOf("{") + 1, propertyName.lastIndexOf("}"));
                                    field.set(bean, getTenantConfig(propertyName, field.getType()));
                                }
                            }
                        }
                        //取出session中的User
                        if (fixMappingRules != null) {
                            ReflectionUtils.makeAccessible(field);
                            Object mappingRules = getMappingRules(fixMappingRules.profile_name(), fixMappingRules.prefix(), field.getType());
                            if (mappingRules != null) {
                                field.set(bean, mappingRules);
                            }
                        }
                    });
                }
                return pvs;
            }

            //todo 加刷新配置文件区分功能
            private Object getTenantConfig(String propertyName, Class type) {
                Long tenantId = getTenantId();
                ResultSet resultSet = null;
                Connection conn = null;
                PreparedStatement preparedStatement = null;
                try {
                    TypeConverter typeConverter = new SimpleTypeConverter();
                    String defaultDbDriverName = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DRIVER_CLASS), String.class);
                    String defaultDbUrl = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DATASOURCE_URL), String.class);
                    String defaultDbUser = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DATASOURCE_USERNAME), String.class);
                    String defaultDdPassword = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DATASOURCE_PASSWORD), String.class);

                    Class.forName(defaultDbDriverName);
                    conn = DriverManager.getConnection(defaultDbUrl, defaultDbUser, defaultDdPassword);
                    String sql = "SELECT prop_value from config_demo where prop_name=? and application=? and profile=? and label=? and del_flag=0";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, propertyName);
                    // application 配置文件名称
                    preparedStatement.setString(2, "saas-service1");
                    preparedStatement.setLong(3, tenantId);
                    //label 服务名称
                    preparedStatement.setString(4, "saas-service1");
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        if (type == Integer.TYPE || type == Integer.class) {
                            return resultSet.getInt("prop_value");
                        } else {
                            return resultSet.getObject("prop_value");
                        }
                    }
                } catch (SQLException e) {
                    log.error("读取项目配置失败！propertyName：{}\nTenantId:{}\nException:{}", propertyName, tenantId, e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeDbSource(null, resultSet, preparedStatement, conn);
                }
                return null;
            }

            private Object getMappingRules(String profileName, String prefix, Class clazz) {
                Long tenantId = getTenantId();
                ResultSet resultSet = null;
                Connection conn = null;
                PreparedStatement preparedStatement = null;
                try {
                    TypeConverter typeConverter = new SimpleTypeConverter();
                    String defaultDbDriverName = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DRIVER_CLASS), String.class);
                    String defaultDbUrl = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DATASOURCE_URL), String.class);
                    String defaultDbUser = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DATASOURCE_USERNAME), String.class);
                    String defaultDdPassword = typeConverter.convertIfNecessary(beanFactory.resolveEmbeddedValue(RefreshUrlConstant.DATASOURCE_PASSWORD), String.class);

                    Class.forName(defaultDbDriverName);
                    conn = DriverManager.getConnection(defaultDbUrl, defaultDbUser, defaultDdPassword);
                    String sql = "SELECT prop_name,prop_value from config_demo where prop_name like ? and application=? and profile=? and label=? and del_flag=0";
                    preparedStatement = conn.prepareStatement(sql);
                    //前缀
                    preparedStatement.setString(1, "%" + prefix + "%");
                    // application 配置文件名称
                    preparedStatement.setString(2, profileName);
                    preparedStatement.setLong(3, tenantId);
                    //label 服务名称
                    preparedStatement.setString(4, "saas-service1");
                    resultSet = preparedStatement.executeQuery();

                    Properties properties = new Properties();
                    while (resultSet.next()) {
                        if (StringUtils.hasText(resultSet.getString("prop_value"))) {
                            properties.put(resultSet.getString("prop_name"), resultSet.getString("prop_value"));
                        }
                    }
                    return propsToBean(properties, prefix, clazz);
                } catch (SQLException e) {
                    log.error("读取项目配置失败！propertyName：{}\nTenantId:{}\nException:{}", profileName, tenantId, e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    closeDbSource(null, resultSet, preparedStatement, conn);
                }
                return null;
            }

            private Long getTenantId() {
                WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
                // 从 webApplicationContext 中获取  servletContext
                ServletContext servletContext = webApplicationContext.getServletContext();
                Long tenantId = (Long) servletContext.getAttribute("tenantId");
                if (null == tenantId) {
                    tenantId = RefreshUrlConstant.DEFAULT_TENANT_ID;
                }
                return tenantId;
            }

            public <T> T propsToBean(Properties properties, String prefix, Class<T> clazz) {
                Enumeration<Object> keys = properties.keys();
                Properties newProps = new Properties();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    if (key.startsWith(prefix + ".")) {
                        newProps.put(key.substring(prefix.length() + 1), properties.get(key));
                    }
                }
                String json = new PropertiesToJsonConverter().convertToJson(newProps);
                T bean = GsonUtil.gsonToBean(json, clazz);
                return bean;
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