package com.example.saasdemo.dynamic;

import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.util.ApplicationContextConfigUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author yubohai
 * @Classname RedisCacheConfig
 * @Date 2021/8/16 17:19
 * scopedTarget.redisCacheConfig
 */
@Configuration
@RefreshScope
public class RedisCacheConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private int database;


    @FixValue(propertyName = "${spring.redis.host}")
    private  String host;

    @FixValue(propertyName = "${spring.redis.port}")
    private  Integer port;

    @FixValue(propertyName = "${spring.redis.password}")
    private  String password;

    @FixValue(propertyName = "${nacos.tenant}")
    private String tenantId;

    @FixValue(propertyName = "${spring.redis.database}")
    private int database1;


    public void updateRedisConfig(String host, int port, String password, int database) {

        WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
        RedisTemplate template = (RedisTemplate) webApplicationContext.getBean("redisTemplate");
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setPassword(password);
        configuration.setDatabase(database);
        template.setConnectionFactory(new JedisConnectionFactory(configuration));
    }

    @Bean
    public JedisConnectionFactory factory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setPassword(redisPassword);
        configuration.setDatabase(database);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(factory());
        return template;
    }

//    @Bean
//    RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter messageListenerAdapter) {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        // 监听所有库的key过期事件
//        container.setConnectionFactory(factory());
//        // 可以添加多个 messageListener，配置不同的通道
//        container.addMessageListener(messageListenerAdapter, new PatternTopic(SYS_NOTIFY_TOPIC));
//        container.addMessageListener(messageListenerAdapter, new PatternTopic(SYS_LOGOUT_TOPIC));
//        container.addMessageListener(messageListenerAdapter, new PatternTopic(TABLE_STRUCTURE_NOTIFY_LIST));
//        return container;
//    }


//    @Bean
//    MessageListenerAdapter listenerAdapter(RedisMessageListener receiver) {
//        return new MessageListenerAdapter(receiver, "onMessage");
//    }

}
