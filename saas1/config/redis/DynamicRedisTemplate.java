package com.config.redis;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

public class DynamicRedisTemplate<K, V> extends RedisTemplate<K, V> {

    @Override
    protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
        return super.createRedisConnectionProxy(pm);
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        Integer db = RedisContext.getSelect();
        if(db != null){
            connection.select(db);
        }else{
            throw new RuntimeException(" no redis db selected ");
        }
        return super.preProcessConnection(connection, existingConnection);
    }


}
