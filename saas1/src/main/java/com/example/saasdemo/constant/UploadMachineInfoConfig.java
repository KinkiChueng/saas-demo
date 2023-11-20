package com.example.saasdemo.constant;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhangjinqi
 */
//@Configuration
//@RefreshScope
//@ConfigurationProperties(prefix = "machine")
@Data
public class UploadMachineInfoConfig {
    List<UploadMachineInfoPo> uploadMachineInfoPoList;

    public List<UploadMachineInfoPo> getUploadMachineInfoPoList() {
        return uploadMachineInfoPoList;
    }


//    public void setUploadMachineInfoPoList(List<UploadMachineInfoPo> uploadMachineInfoPoList) {
//        this.uploadMachineInfoPoList = uploadMachineInfoPoList;
//    }
}
