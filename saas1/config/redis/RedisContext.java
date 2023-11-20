package com.config.redis;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisContext {

    private static final InheritableThreadLocal<Integer> REDIS_SELECT_CONTEXT = new InheritableThreadLocal<>();

    public static void select(Integer db){
        REDIS_SELECT_CONTEXT.set(db);
    }

    public static Integer getSelect(){
        return REDIS_SELECT_CONTEXT.get();
    }

}
