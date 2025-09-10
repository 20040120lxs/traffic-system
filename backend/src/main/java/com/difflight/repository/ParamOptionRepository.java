package com.difflight.repository;

import com.difflight.entity.ParamOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参数选项仓储接口
 */
@Repository
public interface ParamOptionRepository extends JpaRepository<ParamOption, Long> {
    
    /**
     * 根据类型查找参数选项，按排序顺序排列
     */
    List<ParamOption> findByTypeOrderBySortOrderAsc(String type);
    
    /**
     * 检查类型和值的组合是否存在
     */
    boolean existsByTypeAndValue(String type, String value);
    
    /**
     * 查找所有参数选项，按类型和排序顺序分组
     */
    @Query("SELECT p FROM ParamOption p ORDER BY p.type, p.sortOrder")
    List<ParamOption> findAllOrderByTypeAndSortOrder();
}