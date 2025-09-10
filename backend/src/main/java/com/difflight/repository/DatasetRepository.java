package com.difflight.repository;

import com.difflight.entity.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 数据集数据访问接口
 * 
 * 提供数据集信息的基本CRUD操作和查询功能
 */
@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    
    /**
     * 根据所有者ID和类型查找数据集
     * @param ownerId 所有者ID
     * @param ownerType 所有者类型（USER/ADMIN）
     * @return 数据集列表
     */
    List<Dataset> findByOwnerIdAndOwnerType(Long ownerId, String ownerType);
    
    /**
     * 查找所有公开的数据集
     * @return 公开数据集列表
     */
    List<Dataset> findByIsPublicTrue();
    
    /**
     * 根据所有者ID查找数据集（不限类型）
     * @param ownerId 所有者ID
     * @return 数据集列表
     */
    List<Dataset> findByOwnerId(Long ownerId);
}