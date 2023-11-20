package com.config.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author DAI
 * @date 2020/5/30 13:55
 * @Description TODO
 */
@Component
public class MongoContext {

    private static final Map<String, MongoDbFactory> MONGO_CLIENT_DB_FACTORY_MAP = new HashMap<>();
    private static final InheritableThreadLocal<MongoDbFactory> MONGO_DB_FACTORY_THREAD_LOCAL = new InheritableThreadLocal<>();
    @Autowired
    MongoListProperties mongoListProperties;

    @PostConstruct
    public void afterPropertiesSet() {
        if (!CollectionUtils.isEmpty(mongoListProperties.getList())) {
            mongoListProperties.getList().forEach(info->{
                MONGO_CLIENT_DB_FACTORY_MAP.put(info.getDatabase(), new SimpleMongoClientDbFactory(info.getUri()));
                System.out.println("========"+info.getDatabase());
            });
        }
    }

    @Bean(name = "mongoTemplate")
    public DynamicMongoTemplate dynamicMongoTemplate() {
        Iterator<MongoDbFactory> iterator = MONGO_CLIENT_DB_FACTORY_MAP.values().iterator();
        return new DynamicMongoTemplate(iterator.next());
    }

    @Bean(name = "mongoDbFactory")
    public MongoDbFactory mongoDbFactory() {
        Iterator<MongoDbFactory> iterator = MONGO_CLIENT_DB_FACTORY_MAP.values().iterator();
        return iterator.next();
    }

    public static void setMongoDbFactory(String name) {
        MONGO_DB_FACTORY_THREAD_LOCAL.set(MONGO_CLIENT_DB_FACTORY_MAP.get(name));
    }

    public static MongoDbFactory getMongoDbFactory() {
        return MONGO_DB_FACTORY_THREAD_LOCAL.get();
    }


    public static void removeMongoDbFactory(){
        MONGO_DB_FACTORY_THREAD_LOCAL.remove();
    }

}