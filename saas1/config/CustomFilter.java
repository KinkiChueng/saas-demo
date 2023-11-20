package com.config;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter implements TypeFilter {

    private static final String PACKAGE = ".web.config.";
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
        // 可以通过MetadataReader获得各种信息，然后根据自己的需求返回boolean，实例表示包名含有aaa路径的类名将满足筛选条件。
        List<String> nameList = new ArrayList<>();
        /*nameList.add("com.frame.core.datasource.DataSourceRegister");
        nameList.add("com.frame.core.datasource.DataSourcePropertyLoader");
        nameList.add("com.frame.core.datasource.ApplicationContextUtil");
        nameList.add("com.frame.core.datasource.RoutingDataSource");*/
        return metadataReader.getClassMetadata().getClassName().contains(PACKAGE)
        & (!nameList.contains(metadataReader.getClassMetadata().getClassName()))
               ;
    }

}


