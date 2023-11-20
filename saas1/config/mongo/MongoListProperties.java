package com.config.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author DAI
 * @date 2020/5/30 20:05
 * @Description TODO
 */
@Configuration
@ConfigurationProperties(prefix = "spring.mongodb")
public class MongoListProperties {

    private List<MongoList> list;

    public static class MongoList {
        private String uri;
        private String database;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }
    }

    public List<MongoList> getList() {
        return list;
    }

    public void setList(List<MongoList> list) {
        this.list = list;
    }
}
