//package com.example.saasdemo.kafka;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.kafka.annotation.KafkaHandler;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
///**
// * 接收监管报送平台作业重跑消息
// *
// * @author zhangjinqi
// * @date 2022-12-06
// */
//@Component
//@Slf4j
//@RefreshScope
//@DependsOn(value = "kafkaTopicConfig")
//@KafkaListener(id = "regulatoryReportGroup", topics = {"${kafka.topic.regulatoryJobTopic}"}, autoStartup = "${listen.auto.start:true}")
//public class KafkaRecordListener {
//
//    private final TaskExecutor exec = new SimpleAsyncTaskExecutor();
//
//    @KafkaHandler
//    public void dataReplenishRecordDo(String a) {
//        log.info("Received: {}",a);
//    }
//
//    @KafkaHandler(isDefault = true)
//    public void unknown(Object object) {
//        log.info("Received unknown: " + object);
//    }
//}
