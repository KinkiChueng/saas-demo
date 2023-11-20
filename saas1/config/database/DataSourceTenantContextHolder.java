package com.config.database;

public class DataSourceTenantContextHolder {
    public static final String DEFAULT_TENANT = "default";
    private static final InheritableThreadLocal<String> DATASOURCE_HOLDER = new InheritableThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return DEFAULT_TENANT;
        }
    };
    //设置默认数据源
    public static void setDefaultTenant() {
        setCurrentTanent(DataSourceTenantContextHolder.DEFAULT_TENANT);
    }
    //获取当前数据源配置租户标识
    public static String getCurrentTenant() {
        return DATASOURCE_HOLDER.get();
    }
    //设置当前数据源配置租户标识
    public static void setCurrentTanent(String tenant) {
        DATASOURCE_HOLDER.set(tenant);
    }
}

