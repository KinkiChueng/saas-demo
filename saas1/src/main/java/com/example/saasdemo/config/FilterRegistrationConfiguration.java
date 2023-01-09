package com.example.saasdemo.config;

import com.example.saasdemo.constant.RefreshUrlConstant;
import com.example.saasdemo.intercepter.RefreshScopeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterRegistrationConfiguration {
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(repeatableFilter());
        registration.addUrlPatterns(RefreshUrlConstant.REFRESH_CONFIG_URL);
        registration.setName(RefreshUrlConstant.FILTER_NAME);
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter repeatableFilter() {
        return new RefreshScopeFilter();
    }
}