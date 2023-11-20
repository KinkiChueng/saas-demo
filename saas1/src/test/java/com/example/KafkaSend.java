package com.example;

import com.example.saasdemo.SaasDemoFirstApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

/**
 * @author zhangjinqi
 * @date 2023年07月14日 17:10
 */
@SpringBootTest(classes = SaasDemoFirstApplication.class)
public class KafkaSend {

    @Resource
    private KafkaTemplate<Object, Object> template;

    @Test
    public void kafkaSend() {
        template.send("mysqlkafka","aaaaaaaaaaaaaaa");
        template.send("mysql2kafka","11111111111111111111111");
    }
}
