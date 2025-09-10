package com.difflight.repository;

import com.difflight.entity.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据集仓储接口
 */
@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    
    /**
     * 根据用户名查找数据集
     */
    List<Dataset> findByUsername(String username);
    
    /**
     * 查找所有公共数据集
     */
    List<Dataset> findByIsPublicTrue();
    
    /**
     * 根据用户名和是否公共查找数据集
     */
    List<Dataset> findByUsernameAndIsPublic(String username, Boolean isPublic);
}