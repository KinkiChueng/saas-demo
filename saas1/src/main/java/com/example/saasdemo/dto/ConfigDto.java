package com.example.saasdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class ConfigDto {
   @JsonFormat
    private Long tenantId;
    private String configName;
    private String configValue;
}
