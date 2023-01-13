package com.example.saas2.intercepter;

import com.example.saas2.constant.RefreshUrlConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhangjinqi
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要拦截的路径，/**表示拦截所有请求
        String[] addPathPatterns={RefreshUrlConstant.WHOLE_URL};
        //不需要拦截的路径
        String[] excludePathPatterns={RefreshUrlConstant.UPDATE_CONFIG_URL, RefreshUrlConstant.REFRESH_CONFIG_URL,RefreshUrlConstant.ERROR_URL};

        registry.addInterceptor(getMybatisInterceptor())
                .addPathPatterns(addPathPatterns)
                .excludePathPatterns(excludePathPatterns);
    }

    @Bean
    TenantIntercepter getMybatisInterceptor(){
        return new TenantIntercepter();
    }

}