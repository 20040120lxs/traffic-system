package com.difflight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DiffLight 交通系统主应用入口
 * 
 * 提供交通数据处理、参数配置、用户管理等功能的后端服务
 */
@SpringBootApplication
public class DiffLightApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffLightApplication.class, args);
    }
}