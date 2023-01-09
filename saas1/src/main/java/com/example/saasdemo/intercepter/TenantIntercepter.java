package com.example.saasdemo.intercepter;

import com.example.saasdemo.api.MetadataFeignApi;
import com.example.saasdemo.util.ApplicationContextConfigUtil;
import com.example.saasdemo.dto.DataSourceDto;
import com.example.saasdemo.dynamic.DataSourceInfoService;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import com.gwmfc.domain.User;
import com.gwmfc.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TenantIntercepter implements HandlerInterceptor {
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
        //将解析的用户结果先放入session中
        request.getSession().setAttribute("currentUser",user);
//        String tenantId = JwtUtil.decode(token, "tenantId");
        DataSourceDto dataSourceDto = metadataFeignApi.getOne(user.getTenantId()).getData();
        dataSourceInfoService.setDataSourceInfo(dataSourceDto);
        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceDto.getCode());


        WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
        // 从 webApplicationContext 中获取  servletContext
        ServletContext servletContext = webApplicationContext.getServletContext();
        Long undateTenantId = (Long) servletContext.getAttribute("tenantId");
        if (null == undateTenantId || !user.getTenantId().equals(undateTenantId)) {
            servletContext.setAttribute("tenantId", user.getTenantId());
            RefreshScope refreshScope = webApplicationContext.getBean(RefreshScope.class);
            // 手动刷新全部配置
            refreshScope.refreshAll();
        }
        return true;
    }
}
