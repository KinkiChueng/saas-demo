package com.example.saas2.intercepter;

import com.example.saas2.constant.MappingDataSourceConstant;
import com.example.saas2.dto.DataSourceDto;
import com.example.saas2.dynamic.DataSourceInfoService;
import com.example.saas2.dynamic.DynamicDataSourceContextHolder;
import com.example.saas2.util.ApplicationContextConfigUtil;
import com.example.saas2.util.MapToObjUtil;
import com.gwmfc.domain.User;
import com.gwmfc.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author zhangjinqi
 */
@Slf4j
public class TenantIntercepter implements HandlerInterceptor {
    @Resource
    private DataSourceInfoService dataSourceInfoService;

    @Autowired
    private MappingDataSourceConstant mappingDataSourceConstant;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //1.获取请求头中的token
        String token = request.getHeader("Authorization");

        //2.校验token合法性
        token = token.split(" ")[1];

        User user = JwtUtil.decodeUser(token);
        //将解析的用户结果先放入session中
        request.getSession().setAttribute("currentUser", user);
//        String tenantId = JwtUtil.decode(token, "tenantId");
        //todo 从当前的配置文件中获取数据库配置参数 注入动态数据源
        Map<Long, Map<String, String>> allDatasourceMap = mappingDataSourceConstant.getDatasource();
        Map<String, String> tenantDataSource = allDatasourceMap.get(user.getTenantId());
        if (null != tenantDataSource && tenantDataSource.size() > 0) {
            DataSourceDto dataSourceDto = MapToObjUtil.mapToObj(tenantDataSource, DataSourceDto.class);
            dataSourceInfoService.setDataSourceInfo(dataSourceDto);
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceDto.getCode());
        }
//        DataSourceDto dataSourceDto = metadataFeignApi.getOne(user.getTenantId()).getData();

        WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
        // 从 webApplicationContext 中获取  servletContext
        ServletContext servletContext = webApplicationContext.getServletContext();
        Long undateTenantId = (Long) servletContext.getAttribute("tenantId");
        if (null == undateTenantId || !user.getTenantId().equals(undateTenantId)) {
            servletContext.setAttribute("tenantId", user.getTenantId());
            RefreshScope refreshScope = webApplicationContext.getBean(RefreshScope.class);
            // 手动刷新全部配置
            //todo 从nacos的不同namespace下读取配置
            refreshScope.refreshAll();
        }
        return true;
    }
}
