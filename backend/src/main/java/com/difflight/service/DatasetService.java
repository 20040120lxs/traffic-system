package com.difflight.service;

import com.difflight.entity.Dataset;
import com.difflight.repository.DatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * 数据集服务类
 * 
 * 处理数据集的上传、存储和管理功能
 */
@Service
public class DatasetService {
    
    private static final Logger logger = LoggerFactory.getLogger(DatasetService.class);
    
    @Autowired
    private DatasetRepository datasetRepository;
    
    @Value("${app.storage.datasets}")
    private String datasetsStoragePath;
    
    /**
     * 上传数据集文件
     * @param file 上传的文件
     * @param userId 上传用户的ID
     * @param userType 用户类型（USER/ADMIN）
     * @return 保存的数据集实体
     * @throws IOException 文件操作异常
     */
    public Dataset uploadDataset(MultipartFile file, Long userId, String userType) throws IOException {
        // 验证文件
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        
        // 创建存储目录（如果不存在）
        Path storageDir = Paths.get(datasetsStoragePath);
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
            logger.info("创建数据集存储目录：{}", storageDir.toAbsolutePath());
        }
        
        // 生成安全的文件名（UUID + 原始文件名）
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String safeFilename = UUID.randomUUID().toString() + fileExtension;
        Path targetPath = storageDir.resolve(safeFilename);
        
        // 保存文件
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("文件保存成功：{}", targetPath.toAbsolutePath());
        
        // 创建数据集实体并保存到数据库
        Dataset dataset = new Dataset();
        dataset.setOwnerId(userId);
        dataset.setOwnerType(userType);
        dataset.setFileName(originalFilename);
        dataset.setFilePath(targetPath.toString());
        dataset.setFileSize(file.getSize());
        dataset.setIsPublic(false); // 默认为私有
        
        Dataset savedDataset = datasetRepository.save(dataset);
        logger.info("数据集信息保存成功，ID：{}", savedDataset.getId());
        
        return savedDataset;
    }
    
    /**
     * 获取用户的数据集列表
     * @param userId 用户ID
     * @param userType 用户类型
     * @return 数据集列表
     */
    public List<Dataset> getUserDatasets(Long userId, String userType) {
        return datasetRepository.findByOwnerIdAndOwnerType(userId, userType);
    }
    
    /**
     * 获取公开的数据集列表
     * @return 公开数据集列表
     */
    public List<Dataset> getPublicDatasets() {
        return datasetRepository.findByIsPublicTrue();
    }
    
    /**
     * 根据ID获取数据集
     * @param datasetId 数据集ID
     * @return 数据集实体
     */
    public Dataset getDatasetById(Long datasetId) {
        return datasetRepository.findById(datasetId).orElse(null);
    }
    
    /**
     * 设置数据集的公开状态（仅管理员可用）
     * @param datasetId 数据集ID
     * @param isPublic 是否公开
     * @return 更新后的数据集
     */
    public Dataset setDatasetPublic(Long datasetId, boolean isPublic) {
        Dataset dataset = datasetRepository.findById(datasetId).orElse(null);
        if (dataset != null) {
            dataset.setIsPublic(isPublic);
            return datasetRepository.save(dataset);
        }
        return null;
    }
}