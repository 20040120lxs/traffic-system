-- 初始化管理员账号
INSERT IGNORE INTO admins (username, password, phone, created_at, updated_at) 
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', '13800000000', NOW(), NOW());

-- 初始化参数选项数据
-- 交通文件选项
INSERT IGNORE INTO param_options (type, value, display_name, sort_order, created_at, updated_at) VALUES
('traffic_file', 'hangzhou', '杭州交通数据', 1, NOW(), NOW()),
('traffic_file', 'jinan', '济南交通数据', 2, NOW(), NOW()),
('traffic_file', 'synthetic_1', '合成数据集1', 3, NOW(), NOW()),
('traffic_file', 'synthetic_2', '合成数据集2', 4, NOW(), NOW());

-- 路网文件选项
INSERT IGNORE INTO param_options (type, value, display_name, sort_order, created_at, updated_at) VALUES
('roadnet_file', 'roadnet_4_4', '4x4路网', 1, NOW(), NOW()),
('roadnet_file', 'roadnet_3_4', '3x4路网', 2, NOW(), NOW()),
('roadnet_file', 'roadnet_6_6', '6x6路网', 3, NOW(), NOW()),
('roadnet_file', 'custom_net_1', '自定义路网1', 4, NOW(), NOW());

-- 交叉路口数选项
INSERT IGNORE INTO param_options (type, value, display_name, sort_order, created_at, updated_at) VALUES
('num_intersections', '16', '16个交叉路口', 1, NOW(), NOW()),
('num_intersections', '12', '12个交叉路口', 2, NOW(), NOW()),
('num_intersections', '36', '36个交叉路口', 3, NOW(), NOW()),
('num_intersections', '24', '24个交叉路口', 4, NOW(), NOW());

-- 缺失模式选项
INSERT IGNORE INTO param_options (type, value, display_name, sort_order, created_at, updated_at) VALUES
('missing_pattern', 'random_missing', '随机缺失', 1, NOW(), NOW()),
('missing_pattern', 'kriging_missing', 'Kriging缺失', 2, NOW(), NOW()),
('missing_pattern', 'block_missing', '块状缺失', 3, NOW(), NOW()),
('missing_pattern', 'temporal_missing', '时序缺失', 4, NOW(), NOW());

-- 缺失率选项
INSERT IGNORE INTO param_options (type, value, display_name, sort_order, created_at, updated_at) VALUES
('missing_rate', '10%', '10%缺失率', 1, NOW(), NOW()),
('missing_rate', '30%', '30%缺失率', 2, NOW(), NOW()),
('missing_rate', '50%', '50%缺失率', 3, NOW(), NOW()),
('missing_rate', '6.25%', '6.25%缺失率', 4, NOW(), NOW()),
('missing_rate', '12.5%', '12.5%缺失率', 5, NOW(), NOW()),
('missing_rate', '18.75%', '18.75%缺失率', 6, NOW(), NOW()),
('missing_rate', '8.33%', '8.33%缺失率', 7, NOW(), NOW()),
('missing_rate', '16.67%', '16.67%缺失率', 8, NOW(), NOW()),
('missing_rate', '25%', '25%缺失率', 9, NOW(), NOW());