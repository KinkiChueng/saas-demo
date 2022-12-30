package com.example.saas2.api;


import com.example.saas2.dto.DataSourceDto;
import com.gwmfc.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhangjinqi
 */
@FeignClient(value = "metadata-service")
public interface MetadataFeignApi {

    /**
     * 数据源详情
     * @param id 参数
     * @return Result
     */
    @GetMapping("/datasource/{id}")
    Result<DataSourceDto> getOne(@PathVariable("id") Long id);

}