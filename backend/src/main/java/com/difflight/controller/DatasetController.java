package com.difflight.controller;

import com.difflight.entity.Dataset;
import com.difflight.service.DatasetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据集控制器
 * 
 * 处理数据集的上传、下载、列表查询等功能
 * 替换原有的桩实现，提供真实的文件上传功能
 */
@RestController
@RequestMapping("/api/datasets")
public class DatasetController {
    
    private static final Logger logger = LoggerFactory.getLogger(DatasetController.class);
    
    @Autowired
    private DatasetService datasetService;
    
    /**
     * 数据集上传接口
     * 
     * 接收用户上传的数据集文件，保存到指定目录并记录到数据库
     * 仅登录用户可访问，支持所有认证用户（普通用户和管理员）
     * 
     * @param file 上传的文件
     * @return 上传结果，包含数据集信息
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDataset(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取当前认证用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("success", false);
                response.put("message", "用户未登录");
                return ResponseEntity.status(401).body(response);
            }
            
            // 从认证对象中获取用户信息
            String username = authentication.getName();
            Long userId = (Long) authentication.getDetails();
            String userType = authentication.getAuthorities().iterator().next().getAuthority();
            
            logger.info("用户 {} (ID: {}, 类型: {}) 正在上传数据集：{}", 
                       username, userId, userType, file.getOriginalFilename());
            
            // 执行文件上传
            Dataset dataset = datasetService.uploadDataset(file, userId, userType);
            
            // 构造成功响应
            response.put("success", true);
            response.put("message", "数据集上传成功");
            response.put("dataset", Map.of(
                "id", dataset.getId(),
                "fileName", dataset.getFileName(),
                "fileSize", dataset.getFileSize(),
                "ownerId", dataset.getOwnerId(),
                "ownerType", dataset.getOwnerType(),
                "isPublic", dataset.getIsPublic(),
                "createdAt", dataset.getCreatedAt()
            ));
            
            logger.info("数据集上传成功，ID：{}", dataset.getId());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("数据集上传失败：{}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "上传失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取我的数据集列表
     * @return 当前用户的数据集列表
     */
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyDatasets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = (Long) authentication.getDetails();
            String userType = authentication.getAuthorities().iterator().next().getAuthority();
            
            List<Dataset> datasets = datasetService.getUserDatasets(userId, userType);
            
            response.put("success", true);
            response.put("datasets", datasets);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取我的数据集列表失败：{}", e.getMessage());
            response.put("success", false);
            response.put("message", "获取数据集列表失败");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取公开数据集列表
     * @return 所有公开的数据集
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getPublicDatasets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Dataset> datasets = datasetService.getPublicDatasets();
            
            response.put("success", true);
            response.put("datasets", datasets);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取公开数据集列表失败：{}", e.getMessage());
            response.put("success", false);
            response.put("message", "获取数据集列表失败");
            return ResponseEntity.status(500).body(response);
        }
    }
}