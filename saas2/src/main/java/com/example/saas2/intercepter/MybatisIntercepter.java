package com.example.saas2.intercepter;

import com.example.saas2.api.MetadataFeignApi;
import com.example.saas2.dto.DataSourceDto;
import com.example.saas2.dynamic.DataSourceInfoService;
import com.example.saas2.dynamic.DynamicDataSourceContextHolder;
import com.gwmfc.domain.User;
import com.gwmfc.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
public class MybatisIntercepter implements HandlerInterceptor {
    @Resource
    private DataSourceInfoService dataSourceInfoService;
    @Resource
    private MetadataFeignApi metadataFeignApi;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //1.获取请求头中的token
        String token = request.getHeader("Authorization");

        //2.校验token合法性
        token = token.split(" ")[1];

        User user = JwtUtil.decodeUser(token);
        String tenantId = JwtUtil.decode(token, "tenantId");
        DataSourceDto dataSourceDto = metadataFeignApi.getOne(user.getTenantId()).getData();
        dataSourceInfoService.setDataSourceInfo(dataSourceDto);
        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceDto.getCode());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("用户认证拦截器postHandle方法执行");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("用户认证拦截器afterCompletion方法执行");
    }

}
