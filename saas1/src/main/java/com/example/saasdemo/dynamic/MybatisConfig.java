package com.example.saasdemo.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangjinqi
 */
@RefreshScope
@Configuration
@MapperScan(basePackages = {"com.example.saasdemo.dao.datasource"}, sqlSessionFactoryRef = "dynamicDataSourceFactory")
public class MybatisConfig {
    @Value("${spring.datasource.cheetahdb.url}")
    private String defaultDbUrl;
    @Value("${spring.datasource.cheetahdb.username}")
    private String defaultDbUser;
    @Value("${spring.datasource.cheetahdb.password}")
    private String defaultDdPassword;
    @Value("${spring.datasource.cheetahdb.driverClassName}")
    private String defaultDbDriverName;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 初始化之初，把本项目的数据源作为默认数据源注入
     *
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();

        DruidDataSource defaultDataSource = new DruidDataSource();
        defaultDataSource.setName("default");
        defaultDataSource.setUrl(defaultDbUrl);
        defaultDataSource.setUsername(defaultDbUser);
        defaultDataSource.setPassword(defaultDdPassword);
        defaultDataSource.setDriverClassName(defaultDbDriverName);

        Map<Object, Object> map = new HashMap<>(1);
        map.put("default", defaultDataSource);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.setDefaultDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    @Bean(name = "dynamicDataSourceFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource,@Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor)
            throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*db/*Mapper.xml"));
        bean.setPlugins(mybatisPlusInterceptor);
        return bean.getObject();

    }

    @Bean(name = "dynamicDataSourceTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("dynamicDataSourceFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
