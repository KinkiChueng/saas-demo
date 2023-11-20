package com.example.saasdemo.controller;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.example.saasdemo.api.MetadataFeignApi;
import com.example.saasdemo.api.SaasFeignApi;
import com.example.saasdemo.custom.annotation.CurrentUser;
import com.example.saasdemo.custom.annotation.FixValue;
import com.example.saasdemo.dao.datasource.DataSourceMapper;
import com.example.saasdemo.dto.DataSourceDto;
import com.example.saasdemo.dynamic.DynamicDataSourceContextHolder;
import com.example.saasdemo.service.ConfigService;
import com.example.saasdemo.util.ApplicationContextConfigUtil;
import com.gwmfc.domain.ConfigDto;
import com.gwmfc.domain.User;
import com.gwmfc.util.GsonUtil;
import com.gwmfc.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjinqi
 */
@Slf4j
@RestController
@RefreshScope
@RequestMapping("/nacos")
public class NacosController {
    @Resource
    private SaasFeignApi saasFeignApi;

    @Resource
    private MetadataFeignApi metadataFeignApi;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private ConfigService configService;

    @Resource
    private KafkaTemplate<Object, Object> template;

    private final NacosNamingService nacosNamingService;

    public NacosController(NacosNamingService nacosNamingService) {
        this.nacosNamingService = nacosNamingService;
    }


    @FixValue(propertyName = "${nacos.tenant}")
    private String tenantId;

    @GetMapping("/getTenantId")
    String selectConfigValue() {
        return dataSourceMapper.selectConfigValue("nacos.tenant", 65L);
    }

    @GetMapping("/getTenantIdByFixValue")
    String selectConfigValueByFixValue() {
        return tenantId;
    }

    @GetMapping("/getTenantIdDefault")
    String selectConfigValueFromDefault(@CurrentUser User user) {
        DynamicDataSourceContextHolder.setDataSourceKey("default");
        String config = dataSourceMapper.selectConfigValue("nacos.tenant", 65L);
        DataSourceDto dataSourceDto = metadataFeignApi.getOne(user.getTenantId()).getData();
        DynamicDataSourceContextHolder.setDataSourceKey(dataSourceDto.getCode());
        return config;
    }

    @GetMapping("/getMySqlInfo")
    Result getMySqlInfo() throws InterruptedException {
//        Thread.sleep(100000l);
        return Result.ok(dataSourceMapper.selectInfo());
    }

    @GetMapping("/getFeignInfo")
    Result getFeignInfo() {
        return Result.ok(String.valueOf(saasFeignApi.answerFeignInfo().getData()));
    }

    @GetMapping("/getServiceTenantId")
    Result getServiceTenantId() {
        return Result.ok(String.valueOf(configService.getTenantId()));
    }

    @PostMapping("/updateConfigValue")
    Result updateConfigValue(@RequestBody ConfigDto configDto) {
        configService.updateConfigValue(configDto);

        try {
            List<Instance> instanceList = nacosNamingService.getAllInstances("metadata-service", "DEFAULT_GROUP");
            instanceList.forEach(instance -> {
                //通知刷新配置
                sendHttpRefreshScopeRequest(instance.getIp(), instance.getPort(), configDto);
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }

        //发送kafka广播请求更新配置
//        template.send("metadata-config-topic",configDto);

        return Result.ok();
    }

    private String sendHttpRefreshScopeRequest(String ip, int port, ConfigDto configDto) {
        //1.创建httpClient，httpPost对象 2.配置请求信息 3.执行post请求 4.获得httpEntity，进行字符转换 5.关闭资源
        String result;
        //这里使用HttpClient创建client，使用面向接口编程
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://" + ip + ":" + port + "/nacos/refreshValueScope");
        httpPost.setConfig(RequestConfig.DEFAULT);
        Map<String, Object> param = new HashMap<>(1);
        param.put("tenantId", configDto.getTenantId());
        param.put("configName", configDto.getConfigName());
        param.put("configValue", configDto.getConfigValue());
        HttpEntity entity = new StringEntity(GsonUtil.toJson(param), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //  实现租户配置刷新，要调用对应服务接口refreshValueScope
    // 需先判断当前调用用户所属租户，如果是配置更新租户则刷新配置
    @PostMapping("/refreshValueScope")
    Result refreshValueScope() {
        WebApplicationContext webApplicationContext = (WebApplicationContext) ApplicationContextConfigUtil.getApplicationContext();
        org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope = webApplicationContext.getBean(org.springframework.cloud.context.scope.refresh.RefreshScope.class);
//        refreshScope.refreshAll();
        return Result.ok(tenantId);
    }

    @PostMapping("/refresh")
    Result refresh() {
        return Result.ok(tenantId);
    }

//    @GetMapping("/deleteConfigValue")
//    Result deleteConfigValue(@RequestParam ConfigDto configDto) {
//        configService.updateConfigValue(configDto);
//        return Result.ok();
//    }
}
