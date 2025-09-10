package com.difflight.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 数据集实体类
 * 
 * 存储用户上传的数据集信息，包括文件路径、所有者、是否公开等
 */
@Entity
@Table(name = "datasets")
public class Dataset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "owner_id", nullable = false)
    @NotNull(message = "所有者ID不能为空")
    private Long ownerId;
    
    @Column(name = "owner_type", nullable = false, length = 10)
    @NotBlank(message = "所有者类型不能为空")
    private String ownerType; // "USER" 或 "ADMIN"
    
    @Column(name = "file_name", nullable = false)
    @NotBlank(message = "文件名不能为空")
    private String fileName;
    
    @Column(name = "file_path", nullable = false)
    @NotBlank(message = "文件路径不能为空")
    private String filePath;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 构造方法
    public Dataset() {}
    
    public Dataset(Long ownerId, String ownerType, String fileName, String filePath, Long fileSize) {
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.isPublic = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getOwnerType() {
        return ownerType;
    }
    
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public Boolean getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}