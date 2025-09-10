package com.difflight.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 下载日志实体类
 */
@Entity
@Table(name = "download_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String username;  // 下载用户账号
    
    @Column(name = "dataset_id", nullable = false)
    private Long datasetId;   // 数据集ID
    
    @Column(name = "download_time")
    private LocalDateTime downloadTime;
    
    @PrePersist
    protected void onCreate() {
        this.downloadTime = LocalDateTime.now();
    }
}