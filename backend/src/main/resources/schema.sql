-- DiffLight 数据库表结构初始化脚本
-- 此脚本会在应用启动时自动执行（如果启用了schema初始化）

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    phone_number VARCHAR(11) COMMENT '手机号',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '用户信息表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '管理员用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    phone_number VARCHAR(11) COMMENT '手机号',
    role VARCHAR(20) DEFAULT 'ADMIN' COMMENT '角色',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '管理员信息表';

-- 数据集表
CREATE TABLE IF NOT EXISTS datasets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL COMMENT '所有者ID',
    owner_type VARCHAR(10) NOT NULL COMMENT '所有者类型（USER/ADMIN）',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    file_size BIGINT COMMENT '文件大小（字节）',
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    description VARCHAR(500) COMMENT '数据集描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_owner (owner_id, owner_type),
    INDEX idx_public (is_public)
) COMMENT '数据集信息表';

-- 数据集下载日志表
CREATE TABLE IF NOT EXISTS download_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '下载用户ID',
    user_type VARCHAR(10) NOT NULL COMMENT '用户类型（USER/ADMIN）',
    dataset_id BIGINT NOT NULL COMMENT '数据集ID',
    download_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '下载时间',
    INDEX idx_user (user_id, user_type),
    INDEX idx_dataset (dataset_id)
) COMMENT '数据集下载日志表';

-- 参数选项表
CREATE TABLE IF NOT EXISTS param_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    param_type VARCHAR(50) NOT NULL COMMENT '参数类型',
    param_value VARCHAR(255) NOT NULL COMMENT '参数值',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (param_type)
) COMMENT '参数选项表';