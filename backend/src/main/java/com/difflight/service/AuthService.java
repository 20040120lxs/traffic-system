package com.difflight.service;

import com.difflight.entity.Admin;
import com.difflight.entity.User;
import com.difflight.repository.AdminRepository;
import com.difflight.repository.UserRepository;
import com.difflight.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证服务类
 * 
 * 处理用户和管理员的登录认证逻辑
 * 确保 JWT 令牌中的角色信息正确设置为 "USER" 或 "ADMIN"
 */
@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果包含 JWT 令牌和用户信息
     */
    public Map<String, Object> loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // 生成 JWT 令牌，角色设置为 "USER"
                String token = jwtUtil.generateToken(user.getUsername(), "USER", user.getId());
                
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("token", token);
                result.put("userType", "USER");
                result.put("userId", user.getId());
                result.put("username", user.getUsername());
                result.put("phoneNumber", user.getPhoneNumber());
                
                return result;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "用户名或密码错误");
        return result;
    }
    
    /**
     * 管理员登录
     * @param username 管理员用户名
     * @param password 密码
     * @return 登录结果包含 JWT 令牌和管理员信息
     */
    public Map<String, Object> loginAdmin(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (passwordEncoder.matches(password, admin.getPassword())) {
                // 生成 JWT 令牌，角色设置为 "ADMIN"
                String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN", admin.getId());
                
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("token", token);
                result.put("userType", "ADMIN");
                result.put("userId", admin.getId());
                result.put("username", admin.getUsername());
                result.put("phoneNumber", admin.getPhoneNumber());
                
                return result;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", "管理员用户名或密码错误");
        return result;
    }
    
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param phoneNumber 手机号
     * @return 注册结果
     */
    public Map<String, Object> registerUser(String username, String password, String phoneNumber) {
        Map<String, Object> result = new HashMap<>();
        
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        
        try {
            userRepository.save(user);
            result.put("success", true);
            result.put("message", "注册成功");
            result.put("userId", user.getId());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册失败：" + e.getMessage());
        }
        
        return result;
    }
}