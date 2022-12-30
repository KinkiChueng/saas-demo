package com.example.saas2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname lDataSourceDto
 * @author zhangjinqi
 * @date 2021/12/8 14:16
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceDto {

    private Long id;

    private String code;

    private String name;

    private String type;

    private String dbName;

    private String schema;

    private String user;

    private String password;

    private String prefix;

    private String ip;

    private String port;

    private String parameter;

    private String createUser;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String updateUser;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> childDatabaseList;

}
