package com.config.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "cache.redis")
public class RedisCacheConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);
    private Map<String, String> ttl = new HashMap<>();

    public Map<String, String> getTtl() {
        return ttl;
    }

    public void setTtl(Map<String, String> ttl) {
        this.ttl = ttl;
    }

    @Resource
    private LettuceConnectionFactory factory;
    @Resource
    private GenericFastJsonRedisSerializer genericFastJsonRedisSerializer;

    /**
     * 配置缓存管理器
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory) {
        //启用连接池
        factory.setShareNativeConnection(false);
        //设置默认策略
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(2))       //设置过期时间,2天
                .disableCachingNullValues()         //禁止缓存null 值
                .serializeKeysWith(keyPair())       //设置key 序列化
                .serializeValuesWith(valuePair());  //设置 value 序列化
        // 返回Redis缓存管理器
        return RedisCacheManager.builder(factory)
                .withCacheConfiguration("default", defaultCacheConfig)
                .withInitialCacheConfigurations(getCacheConfiguration())
                .build();
    }

    public Map<String, RedisCacheConfiguration> getCacheConfiguration() {
        Map<String, RedisCacheConfiguration> map = new HashMap<>();
        Iterator it = ttl.entrySet().iterator();
        String cacheName;
        String ttl;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            cacheName = (String) entry.getKey();
            ttl = (String) entry.getValue();
            RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(Long.parseLong(ttl.trim())))
                    .disableCachingNullValues()
                    .serializeKeysWith(keyPair())
                    .serializeValuesWith(valuePair());
            logger.info("cacheName={},ttl={}", cacheName, ttl);
            map.put(cacheName, cacheConfig);
        }
        return map;
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName()).append(".");
            sb.append(method.getName()).append(".");
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 用于显示api调用
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new DynamicRedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        // 用于捕获从Cache中进行CRUD时的异常的回调处理器。
        return new SimpleCacheErrorHandler();
    }

    @Bean
    public GenericFastJsonRedisSerializer genericFastJsonRedisSerializer() {
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        return genericFastJsonRedisSerializer;
    }

    /**
     * 配置键序列化
     */
    private RedisSerializationContext.SerializationPair<String> keyPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    /**
     * 配置值序列化,使用GenericFastJsonRedisSerializer替换默认序列化
     */
    private RedisSerializationContext.SerializationPair<Object> valuePair() {
        //Jackson2JsonRedisSerializer无法正常反序列化成bean,只能序列化成普通的json,因为不知道要反序列化那个类的对象,因为构造方法一般指定为Object.class
        return RedisSerializationContext.SerializationPair.fromSerializer(genericFastJsonRedisSerializer);
    }
}

