package com.difflight.controller;

import com.difflight.dto.LoginRequest;
import com.difflight.dto.LoginResponse;
import com.difflight.dto.UserRegisterRequest;
import com.difflight.entity.Admin;
import com.difflight.entity.User;
import com.difflight.repository.AdminRepository;
import com.difflight.repository.UserRepository;
import com.difflight.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserRegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证确认密码
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                response.put("success", false);
                response.put("message", "密码和确认密码不一致");
                return ResponseEntity.badRequest().body(response);
            }

            // 检查用户名是否已存在
            if (userRepository.existsByUsername(request.getUsername())) {
                response.put("success", false);
                response.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(response);
            }

            // 检查手机号是否已存在
            if (request.getPhone() != null && !request.getPhone().isEmpty() 
                && userRepository.existsByPhone(request.getPhone())) {
                response.put("success", false);
                response.put("message", "手机号已存在");
                return ResponseEntity.badRequest().body(response);
            }

            // 创建新用户
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhone(request.getPhone());

            userRepository.save(user);

            response.put("success", true);
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
            
            if (userOpt.isPresent() && passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
                User user = userOpt.get();
                String token = jwtUtil.generateToken(user.getUsername(), "USER");
                
                LoginResponse loginResponse = new LoginResponse(token, user.getUsername(), "USER", user.getPhone());
                
                response.put("success", true);
                response.put("message", "登录成功");
                response.put("data", loginResponse);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "登录失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 管理员登录
     */
    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@Validated @RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Admin> adminOpt = adminRepository.findByUsername(request.getUsername());
            
            if (adminOpt.isPresent() && passwordEncoder.matches(request.getPassword(), adminOpt.get().getPassword())) {
                Admin admin = adminOpt.get();
                String token = jwtUtil.generateToken(admin.getUsername(), "ADMIN");
                
                LoginResponse loginResponse = new LoginResponse(token, admin.getUsername(), "ADMIN", admin.getPhone());
                
                response.put("success", true);
                response.put("message", "管理员登录成功");
                response.put("data", loginResponse);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "管理员用户名或密码错误");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "管理员登录失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}