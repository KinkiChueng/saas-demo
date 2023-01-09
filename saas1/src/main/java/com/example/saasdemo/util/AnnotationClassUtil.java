package com.example.saasdemo.util;

import com.example.saasdemo.custom.annotation.FixValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AnnotationClassUtil {
    private final static String BASE_PACKAGE = "com.example.saasdemo";
    private final static String RESOURCE_PATTERN = "/**/*.class";


    public static List<String> searchLabelContainedClass() {
        List<String> classList = new ArrayList<>();

        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            //MetadataReader 的工厂类
            MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerfactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    FixValue anno = field.getAnnotation(FixValue.class);
                    if (anno != null) {
                        //将注解中的类型值作为key，对应的类作为 value
                        classList.add(classname);
                        break;
                    }
                }
            }
            return classList;
        } catch (Exception e) {
            log.error("");
        }
        return null;
    }
}
