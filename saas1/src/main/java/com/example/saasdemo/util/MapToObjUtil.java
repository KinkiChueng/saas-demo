package com.example.saasdemo.util;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author zhangjinqi
 */
public class MapToObjUtil {
    /**
     * map转对象
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    public static <T> T mapToObj(Map source, Class<T> target) throws Exception {
        Field[] fields = target.getDeclaredFields();
        T o = target.newInstance();
        for(Field field:fields){
            Object val;
            if((val=source.get(field.getName()))!=null){
                field.setAccessible(true);
                field.set(o,val);
            }
        }
        return o;
    }
}
