//package com.example.saasdemo.intercepter;
//
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.example.saasdemo.util.YmlUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//
//@Component
//@Slf4j
//public class SaasIntercepter implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        log.info("用户认证拦截器preHandle方法执行");
//        String tenantid = request.getParameter("tenantId");
//        String serverAddr = "10.50.133.144:8848";
//        String dataId = "saas-service1-a.yaml";
////        dataId = dataId.replace("{#}",tenantid);
//        String group = "DEFAULT_GROUP";
//        Properties properties = new Properties();
//        properties.put("serverAddr", serverAddr);
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        String content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);
//        YmlUtils.yamlStr2PropStr(content);
//        configService.addListener(dataId, group, new Listener() {
//            @Override
//            public void receiveConfigInfo(String configInfo) {
//                System.out.println("recieve1:" + configInfo);
//            }
//
//            @Override
//            public Executor getExecutor() {
//                return null;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.info("用户认证拦截器postHandle方法执行");
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        log.info("用户认证拦截器afterCompletion方法执行");
//    }
//
//}
