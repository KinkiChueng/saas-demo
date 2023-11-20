package com.example.saasdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author yubohai
 * @Classname RedisUtil
 * @Date 2021/8/17 8:39
 */

@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置缓存key value
     * @param key
     * @param value
     * @return
     */

    public <T> boolean set( String key, T value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
            } catch (Exception e) {
                log.error("set key {} exception {}",key,e);
            }
        return result;
    }

    /**
     * 设置带有超市时间的key value
     * @param key
     * @param value
     * @param expireTime
     * @return
     */

    public <T> boolean set(final String key, T value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("set key {} with expire time exception  {}",key,e);
        }
        return result;
    }

    public <T> T get(final String key) {
        T result = null;
        ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }


    public void publishMsg(String channel,Object data){
        redisTemplate.convertAndSend(channel,data);
    }


}
