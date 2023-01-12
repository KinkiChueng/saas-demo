package com.example.saasdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author zhangjinqi
 */
@Data
public class ConfigDto {
   @JsonFormat
    private Long tenantId;
    private String configName;
    private String configValue;
}
