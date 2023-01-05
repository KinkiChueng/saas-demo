package com.example.saasdemo.config;

import com.example.saasdemo.custom.annotation.CurrentUser;
import com.gwmfc.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class CurrentUserHandlerMethodArgReslover implements HandlerMethodArgumentResolver {

    /**
    * 判断是否支持使用@CurrentUser注解的参数
    */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //如果该参数注解有@CurrentUser且参数类型是User
        return methodParameter.getParameterAnnotation(CurrentUser.class) != null &&methodParameter.getParameterType() == User.class;
    }

    /**
    * 注入参数值
    */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //取得HttpServletRequest
        HttpServletRequest request= (HttpServletRequest) nativeWebRequest.getNativeRequest();
        //取出session中的User
        return request.getSession().getAttribute("currentUser");
    }
}