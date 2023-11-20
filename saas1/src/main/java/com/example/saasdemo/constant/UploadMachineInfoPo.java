package com.example.saasdemo.constant;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangjinqi
 */
@Data
public class UploadMachineInfoPo implements Serializable {
    private String ip;

    private String username;

    private String password;
}
