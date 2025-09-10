package com.difflight.controller;

import com.difflight.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 
 * 处理用户和管理员的登录、注册请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 用户登录
     * @param loginRequest 登录请求（包含用户名和密码）
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "用户名和密码不能为空"
            ));
        }
        
        Map<String, Object> result = authService.loginUser(username, password);
        
        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }
    
    /**
     * 管理员登录
     * @param loginRequest 登录请求（包含用户名和密码）
     * @return 登录结果
     */
    @PostMapping("/admin/login")
    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "用户名和密码不能为空"
            ));
        }
        
        Map<String, Object> result = authService.loginAdmin(username, password);
        
        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(401).body(result);
        }
    }
    
    /**
     * 用户注册
     * @param registerRequest 注册请求（包含用户名、密码、手机号）
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String confirmPassword = registerRequest.get("confirmPassword");
        String phoneNumber = registerRequest.get("phoneNumber");
        
        // 基本验证
        if (username == null || password == null || phoneNumber == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "用户名、密码和手机号不能为空"
            ));
        }
        
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "两次输入的密码不一致"
            ));
        }
        
        Map<String, Object> result = authService.registerUser(username, password, phoneNumber);
        
        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}