package com.config.mongo;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author DAI
 * @date 2020/5/30 12:51
 * @Description 动态mongo模板
 */
public class DynamicMongoTemplate extends MongoTemplate {

    public DynamicMongoTemplate(MongoDbFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    protected MongoDatabase doGetDatabase() {
        MongoDbFactory mongoDbFactory = MongoContext.getMongoDbFactory();
        return mongoDbFactory.getDb();
        //return mongoDbFactory == null ? super.doGetDatabase() : mongoDbFactory.getDb();
    }
}
