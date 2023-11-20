package com.example.saasdemo.controller;

import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.dynamic.RedisCacheConfig;
import com.example.saasdemo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangjinqi
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/nacos")
public class RedisController {
    @Resource
    private RedisCacheConfig redisCacheConfig;

    @FixValue(propertyName = "${spring.redis.host}")
    private  String host;

    @FixValue(propertyName = "${spring.redis.port}")
    private  Integer port;

    @FixValue(propertyName = "${spring.redis.password}")
    private  String password;

    @FixValue(propertyName = "${nacos.tenant}")
    private String tenantId;

    @FixValue(propertyName = "${spring.redis.database}")
    private int database;

    @Resource
    private RedisUtil redisUtil;


    @GetMapping("/redisTest")
    String redisTest() {
        redisCacheConfig.updateRedisConfig(host,port,password,database);
        redisUtil.set("saas_testr",tenantId);
        return redisUtil.get("saas_testr").toString();
    }


}
