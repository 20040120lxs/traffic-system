package com.difflight.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 结果图片实体类
 */
@Entity
@Table(name = "result_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "parameter_entry_id", nullable = false)
    private Long parameterEntryId;    // 参数条目ID
    
    @Column(name = "image_type", nullable = false, length = 20)
    private String imageType;         // 图片类型：填补结果图、加噪过程图、去噪过程图
    
    @Column(nullable = false)
    private String filename;          // 图片文件名
    
    @Column(nullable = false)
    private String filepath;          // 图片文件路径
    
    @Column(length = 200)
    private String title;             // 自定义标题
    
    @Column(name = "upload_time")
    private LocalDateTime uploadTime;
    
    @PrePersist
    protected void onCreate() {
        this.uploadTime = LocalDateTime.now();
    }
}