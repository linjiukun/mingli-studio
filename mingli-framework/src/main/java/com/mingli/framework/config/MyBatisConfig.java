package com.mingli.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.mingli.system.mapper", "com.mingli.fortune.mapper"})
public class MyBatisConfig {
}
