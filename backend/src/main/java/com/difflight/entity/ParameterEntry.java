package com.difflight.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 参数条目实体类
 */
@Entity
@Table(name = "parameter_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "traffic_file", nullable = false, length = 100)
    private String trafficFile;      // 交通文件名
    
    @Column(name = "roadnet_file", nullable = false, length = 100)
    private String roadnetFile;      // 路网文件名
    
    @Column(name = "num_intersections", nullable = false)
    private Integer numIntersections; // 交叉路口数
    
    @Column(name = "missing_pattern", nullable = false, length = 50)
    private String missingPattern;    // 缺失模式
    
    @Column(name = "missing_rate", nullable = false, length = 20)
    private String missingRate;       // 缺失率
    
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