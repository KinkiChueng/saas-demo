//用于存储每个租户的基本信息 只有主库会存
CREATE TABLE `saas_config` (
                               `property_name` varchar(100) DEFAULT NULL,
                               `value` varchar(100) DEFAULT NULL,
                               `tenant_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                               `service_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
INSERT INTO metadata.saas_config (property_name,value,tenant_id,service_name) VALUES
	 ('nacos.tenant','xfa','95',NULL),
	 ('nacos.tenant','jjjj','96',NULL);

//用于模拟 每个租户不同库表的数据存储
CREATE TABLE `saas_test` (
    `Column1` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci