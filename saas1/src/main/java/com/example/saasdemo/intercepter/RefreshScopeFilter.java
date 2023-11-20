package com.example.saasdemo.intercepter;

import com.example.saasdemo.util.AnnotationClassUtil;
import com.example.saasdemo.util.ApplicationContextConfigUtil;
import com.example.saasdemo.util.StringUtil;
import com.gwmfc.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 刷新单个配置
 * @author zhangjinqi
 */
@Slf4j
public class RefreshScopeFilter implements Filter {

    public static String ReadAsChars(ServletRequest httpServletRequest) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = httpServletRequest.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            log.error("RefreshScopeFilter request deserialization wrong!{}", e);
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
            RefreshScope refreshScope = webApplicationContext.getBean(RefreshScope.class);
            String requestBody = ReadAsChars(servletRequest);
            Map configDto = GsonUtil.jsonToMap(requestBody);

            if (configDto != null) {
                // 只有之前调过该项目其余接口 tenantId值才能获取
                ServletContext servletContext = webApplicationContext.getServletContext();
                Long tenantId = (Long) servletContext.getAttribute("tenantId");

//                if (null != tenantId && null != configDto && tenantId.equals(((Double) configDto.get("tenant_id")).longValue())) {
                    List<String> classPathList = AnnotationClassUtil.searchLabelContainedClass();
                    classPathList.forEach(classPath -> {
                        String className = classPath.substring(classPath.lastIndexOf(".")+1);
                        if (StringUtils.hasText(className)) {
                            refreshScope.refresh(StringUtil.toLowerCaseFirstOne(className));
                        }
                    });
//                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("刷新@FixValue配置失败！{}", e);
        }
    }
}
