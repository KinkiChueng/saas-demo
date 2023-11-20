package com.example.saasdemo.custom.annotation;

import java.lang.annotation.*;

/**
 * @author zhangjinqi
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixMappingRules {
    String prefix();
    String profile_name();
}
