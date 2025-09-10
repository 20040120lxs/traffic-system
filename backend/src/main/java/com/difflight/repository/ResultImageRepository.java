package com.difflight.repository;

import com.difflight.entity.ResultImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结果图片仓储接口
 */
@Repository
public interface ResultImageRepository extends JpaRepository<ResultImage, Long> {
    
    /**
     * 根据参数条目ID查找结果图片
     */
    List<ResultImage> findByParameterEntryId(Long parameterEntryId);
    
    /**
     * 根据参数条目ID和图片类型查找结果图片
     */
    List<ResultImage> findByParameterEntryIdAndImageType(Long parameterEntryId, String imageType);
}