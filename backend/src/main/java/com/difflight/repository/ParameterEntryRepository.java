package com.difflight.repository;

import com.difflight.entity.ParameterEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 参数条目仓储接口
 */
@Repository
public interface ParameterEntryRepository extends JpaRepository<ParameterEntry, Long> {
    
    /**
     * 根据参数组合查找参数条目
     */
    Optional<ParameterEntry> findByTrafficFileAndRoadnetFileAndNumIntersectionsAndMissingPatternAndMissingRate(
            String trafficFile, String roadnetFile, Integer numIntersections, String missingPattern, String missingRate);
}