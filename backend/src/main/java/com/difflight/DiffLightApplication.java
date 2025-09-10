package com.difflight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DiffLight 全栈项目主启动类
 * 
 * @author DiffLight Team
 */
@SpringBootApplication
public class DiffLightApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffLightApplication.class, args);
        System.out.println("=== DiffLight 后端服务启动成功 ===");
        System.out.println("API 文档地址: http://localhost:8080/api");
    }
}