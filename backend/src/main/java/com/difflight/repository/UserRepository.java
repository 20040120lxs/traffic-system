package com.difflight.repository;

import com.difflight.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 
 * 提供用户信息的基本CRUD操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户信息（可选）
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 根据手机号查找用户
     * @param phoneNumber 手机号
     * @return 用户信息（可选）
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}