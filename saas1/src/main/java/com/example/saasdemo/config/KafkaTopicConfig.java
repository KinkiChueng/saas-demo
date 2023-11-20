package com.example.saasdemo.config;

import com.example.saasdemo.custom.annotation.FixValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjinqi
 * @date 2023年07月14日 16:54
 */
@Configuration
@RefreshScope
public class KafkaTopicConfig implements InitializingBean {

    @FixValue(propertyName = "${kafka.topic.regulatoryJobTopic}")
    private String regulatoryJobTopic;

    @Override
    public void afterPropertiesSet() {
//        System.setProperty("topics", "changekafka");
        System.setProperty("topics", regulatoryJobTopic);
    }
}
