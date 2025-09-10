package com.difflight.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 参数选项实体类
 */
@Entity
@Table(name = "param_options", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"type", "value"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String type;              // 参数类型：traffic_file, roadnet_file, num_intersections, missing_pattern, missing_rate
    
    @Column(nullable = false, length = 100)
    private String value;             // 参数值
    
    @Column(name = "display_name", length = 100)
    private String displayName;       // 显示名称（可选）
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;    // 排序顺序
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}