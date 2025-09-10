package com.difflight.controller;

import com.difflight.entity.ParamOption;
import com.difflight.repository.ParamOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 管理员参数选项控制器
 */
@RestController
@RequestMapping("/admin/param-options")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class AdminParamOptionController {

    @Autowired
    private ParamOptionRepository paramOptionRepository;

    /**
     * 获取所有参数选项
     */
    @GetMapping
    public ResponseEntity<?> getAllParamOptions() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<ParamOption> options = paramOptionRepository.findAllOrderByTypeAndSortOrder();
            response.put("success", true);
            response.put("data", options);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取参数选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 根据类型获取参数选项
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getParamOptionsByType(@PathVariable String type) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<ParamOption> options = paramOptionRepository.findByTypeOrderBySortOrderAsc(type);
            response.put("success", true);
            response.put("data", options);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取参数选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 创建新的参数选项
     */
    @PostMapping
    public ResponseEntity<?> createParamOption(@RequestBody ParamOption paramOption) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 检查是否已存在相同的类型和值
            if (paramOptionRepository.existsByTypeAndValue(paramOption.getType(), paramOption.getValue())) {
                response.put("success", false);
                response.put("message", "该类型和值的组合已存在");
                return ResponseEntity.badRequest().body(response);
            }

            ParamOption saved = paramOptionRepository.save(paramOption);
            response.put("success", true);
            response.put("message", "参数选项创建成功");
            response.put("data", saved);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建参数选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 更新参数选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateParamOption(@PathVariable Long id, @RequestBody ParamOption paramOption) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<ParamOption> existingOpt = paramOptionRepository.findById(id);
            if (!existingOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "参数选项不存在");
                return ResponseEntity.badRequest().body(response);
            }

            ParamOption existing = existingOpt.get();
            
            // 检查是否已存在相同的类型和值（排除当前记录）
            if (!existing.getType().equals(paramOption.getType()) || 
                !existing.getValue().equals(paramOption.getValue())) {
                if (paramOptionRepository.existsByTypeAndValue(paramOption.getType(), paramOption.getValue())) {
                    response.put("success", false);
                    response.put("message", "该类型和值的组合已存在");
                    return ResponseEntity.badRequest().body(response);
                }
            }

            // 更新字段
            existing.setType(paramOption.getType());
            existing.setValue(paramOption.getValue());
            existing.setDisplayName(paramOption.getDisplayName());
            existing.setSortOrder(paramOption.getSortOrder());

            ParamOption saved = paramOptionRepository.save(existing);
            response.put("success", true);
            response.put("message", "参数选项更新成功");
            response.put("data", saved);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新参数选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 删除参数选项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParamOption(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (!paramOptionRepository.existsById(id)) {
                response.put("success", false);
                response.put("message", "参数选项不存在");
                return ResponseEntity.badRequest().body(response);
            }

            paramOptionRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "参数选项删除成功");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除参数选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}