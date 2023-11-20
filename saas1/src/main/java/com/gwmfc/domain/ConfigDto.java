package com.gwmfc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author zhangjinqi
 */
@Data
public class ConfigDto {
    private Long tenantId;
    private String configName;
    private String configValue;
}
