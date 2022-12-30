package com.example.saasdemo.config;

import com.example.saasdemo.intercepter.MybatisIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要拦截的路径，/**表示拦截所有请求
        String[] addPathPatterns={"/**"};
        //不需要拦截的路径
//        String[] excludePathPatterns={"/boot/login","/boot/exit"};

        registry.addInterceptor(getMybatisInterceptor())
                .addPathPatterns(addPathPatterns);
//                .excludePathPatterns(excludePathPatterns);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserHandlerMethodArgReslover());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Bean
    MybatisIntercepter getMybatisInterceptor(){
        return new MybatisIntercepter();
    }

}