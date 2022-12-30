package com.example.saasdemo.api;


import com.example.saasdemo.config.FeignConfig;
import com.gwmfc.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangjinqi
 */
@FeignClient(value = "saas-service2",configuration={FeignConfig.class})
public interface SaasFeignApi {

    @GetMapping("/nacos/answerFeignInfo")
    Result answerFeignInfo();

}