package com.example.saasdemo.custom.annotation;

import java.lang.annotation.*;

/**
 * @author zhangjinqi
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixValue {
    String propertyName();
}
