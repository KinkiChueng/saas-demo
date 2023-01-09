package com.example.saas2.dynamic;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangjinqi
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    private DynamicDataSourceContextHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>() {
        /**
         * 将 master 数据源的 key作为默认数据源的 key
         */
        @Override
        protected String initialValue() {
            return "default";
        }
    };

    /**
     * 数据源的 key集合，用于切换时判断数据源是否存在
     */
    public static Set<Object> dataSourceKeys = new HashSet<>();

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSourceKey(String key, String schema) {
        if (null != schema) {
            CONTEXT_HOLDER.set(key + "_" + schema);
        } else {
            CONTEXT_HOLDER.set(key);
        }
    }

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSourceKey(String key) {
        log.info("dataSourceKeys.toString():{}",dataSourceKeys.toString());
        CONTEXT_HOLDER.set(key);
    }

    /**
     * 获取数据源
     * @return
     */
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 重置数据源
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * 判断是否包含数据源
     * @param key 数据源key
     * @return
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }

    /**
     * 添加数据源keys
     * @param keys
     * @return
     */
    public static boolean addDataSourceKeys(Collection<? extends Object> keys) {
        return dataSourceKeys.addAll(keys);
    }

}
