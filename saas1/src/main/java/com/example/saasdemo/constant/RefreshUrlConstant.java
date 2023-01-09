package com.example.saasdemo.constant;

public class RefreshUrlConstant {
    public static final String REFRESH_CONFIG_URL = "/nacos/refreshValueScope";
    public static final String UPDATE_CONFIG_URL = "/nacos/updateConfigValue";
    public static final String ERROR_URL = "/error";
    public static final String WHOLE_URL = "/**";

    public static final String FILTER_NAME = "refreshScopeFilter";

    public static final String DRIVER_CLASS = "${spring.datasource.cheetahdb.driverClassName}";
    public static final String DATASOURCE_URL = "${spring.datasource.cheetahdb.url}";
    public static final String DATASOURCE_USERNAME = "${spring.datasource.cheetahdb.username}";
    public static final String DATASOURCE_PASSWORD = "${spring.datasource.cheetahdb.password}";

    public static final Long DEFAULT_TENANT_ID = 65L;
}
