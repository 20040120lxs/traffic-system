package com.difflight.repository;

import com.difflight.entity.DownloadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 下载日志仓储接口
 */
@Repository
public interface DownloadLogRepository extends JpaRepository<DownloadLog, Long> {
    
    /**
     * 根据用户名查找下载日志
     */
    List<DownloadLog> findByUsername(String username);
    
    /**
     * 根据数据集ID查找下载日志
     */
    List<DownloadLog> findByDatasetId(Long datasetId);
}