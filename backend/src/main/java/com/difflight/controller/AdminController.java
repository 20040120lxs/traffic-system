package com.difflight.controller;

import com.difflight.entity.Dataset;
import com.difflight.service.DatasetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 * 
 * 处理管理员相关的操作，包括数据集管理、用户管理等
 * 需要 ADMIN 权限才能访问
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private DatasetService datasetService;
    
    /**
     * 获取所有数据集列表（管理员视图）
     * @return 所有数据集信息
     */
    @GetMapping("/datasets")
    public ResponseEntity<Map<String, Object>> getAllDatasets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 这里可以扩展为获取所有数据集，暂时返回公开数据集
            List<Dataset> datasets = datasetService.getPublicDatasets();
            
            response.put("success", true);
            response.put("datasets", datasets);
            response.put("message", "获取数据集列表成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取数据集列表失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 设置数据集公开状态
     * @param datasetId 数据集ID
     * @param request 请求体，包含 isPublic 字段
     * @return 操作结果
     */
    @PutMapping("/datasets/{datasetId}/public")
    public ResponseEntity<Map<String, Object>> setDatasetPublic(
            @PathVariable Long datasetId,
            @RequestBody Map<String, Boolean> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Boolean isPublic = request.get("isPublic");
            if (isPublic == null) {
                response.put("success", false);
                response.put("message", "isPublic 参数不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            Dataset dataset = datasetService.setDatasetPublic(datasetId, isPublic);
            if (dataset != null) {
                response.put("success", true);
                response.put("message", isPublic ? "数据集已设置为公开" : "数据集已设置为私有");
                response.put("dataset", dataset);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "数据集不存在");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "操作失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 管理员权限测试接口
     * @return 简单的确认消息
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testAdminAccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "管理员权限验证成功");
        response.put("role", "ADMIN");
        
        return ResponseEntity.ok(response);
    }
}