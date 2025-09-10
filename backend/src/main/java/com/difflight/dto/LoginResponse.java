package com.difflight.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    
    private String token;        // JWT令牌
    private String username;     // 用户名
    private String role;         // 角色：USER 或 ADMIN
    private String phone;        // 手机号
}