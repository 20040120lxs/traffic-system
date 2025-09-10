package com.difflight.repository;

import com.difflight.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 管理员数据访问接口
 * 
 * 提供管理员信息的基本CRUD操作
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    /**
     * 根据用户名查找管理员
     * @param username 管理员用户名
     * @return 管理员信息（可选）
     */
    Optional<Admin> findByUsername(String username);
    
    /**
     * 检查管理员用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 根据手机号查找管理员
     * @param phoneNumber 手机号
     * @return 管理员信息（可选）
     */
    Optional<Admin> findByPhoneNumber(String phoneNumber);
}