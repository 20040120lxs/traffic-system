package com.difflight.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据集实体类
 */
@Entity
@Table(name = "datasets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String username;  // 上传用户账号
    
    @Column(nullable = false)
    private String filename;  // 文件名
    
    @Column(nullable = false)
    private String filepath;  // 文件路径
    
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;  // 是否为公共数据集
    
    @Column(name = "file_size")
    private Long fileSize;    // 文件大小（字节）
    
    @Column(name = "upload_time")
    private LocalDateTime uploadTime;
    
    @PrePersist
    protected void onCreate() {
        this.uploadTime = LocalDateTime.now();
    }
}