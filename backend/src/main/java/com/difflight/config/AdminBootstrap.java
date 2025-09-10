package com.difflight.config;

import com.difflight.entity.Admin;
import com.difflight.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 管理员初始化启动器
 * 
 * 应用启动时自动检查并初始化默认管理员账户
 * 如果数据库中不存在 admin 用户，则自动创建默认管理员
 */
@Component
public class AdminBootstrap implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrap.class);
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // 默认管理员信息
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin@123";
    private static final String DEFAULT_ADMIN_PHONE = "13800000000";
    
    @Override
    public void run(String... args) throws Exception {
        initializeDefaultAdmin();
    }
    
    /**
     * 初始化默认管理员账户
     * 
     * 检查数据库中是否存在 admin 用户，如果不存在则创建
     * 使用 BCrypt 加密密码，确保安全性
     */
    private void initializeDefaultAdmin() {
        logger.info("检查默认管理员账户是否存在...");
        
        // 检查是否已存在 admin 用户
        if (!adminRepository.existsByUsername(DEFAULT_ADMIN_USERNAME)) {
            logger.info("未找到默认管理员账户，开始创建...");
            
            // 创建默认管理员
            Admin defaultAdmin = new Admin();
            defaultAdmin.setUsername(DEFAULT_ADMIN_USERNAME);
            // 使用 BCrypt 加密密码
            defaultAdmin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
            defaultAdmin.setPhoneNumber(DEFAULT_ADMIN_PHONE);
            defaultAdmin.setRole("ADMIN");
            
            try {
                adminRepository.save(defaultAdmin);
                logger.info("默认管理员账户创建成功：username={}, phone={}", 
                           DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PHONE);
                logger.info("默认密码：{}", DEFAULT_ADMIN_PASSWORD);
            } catch (Exception e) {
                logger.error("创建默认管理员账户失败：{}", e.getMessage());
            }
        } else {
            logger.info("默认管理员账户已存在，跳过初始化");
        }
    }
}