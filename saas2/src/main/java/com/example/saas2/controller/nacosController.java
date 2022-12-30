package com.example.saas2.controller;

import com.example.saas2.datasource.DataSourceMapper;
import com.gwmfc.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/nacos")
@RefreshScope
public class nacosController {

    @Resource
    private DataSourceMapper dataSourceMapper;


    @GetMapping("/answerFeignInfo")
    Result answerFeignInfo() {
        Result result = new Result();
        result.setData(dataSourceMapper.selectInfo());
        return result;
    }
}
