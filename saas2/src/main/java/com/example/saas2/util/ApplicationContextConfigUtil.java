package com.example.saas2.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


@Configuration
public class ApplicationContextConfigUtil implements ServletContextListener {


    private static WebApplicationContext webApplicationContext;

    public ApplicationContextConfigUtil() {
        super();
    }

    public void contextInitialized(ServletContextEvent event) {
        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
    }


    public void contextDestroyed(ServletContextEvent event) {
    }

    public static ApplicationContext getApplicationContext() {
        return webApplicationContext;
    }


}