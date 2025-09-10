-- DiffLight 初始数据脚本
-- 此脚本会在表创建后执行，插入初始参数选项等数据

-- 插入默认参数选项（如果不存在）
INSERT IGNORE INTO param_options (param_type, param_value) VALUES
('traffic_file', 'traffic_flow_sample.csv'),
('traffic_file', 'vehicle_trajectory_data.csv'),
('traffic_file', 'intersection_data.csv'),
('roadnet_file', 'citywide_network.json'),
('roadnet_file', 'highway_network.json'),
('roadnet_file', 'urban_grid.json'),
('NUM_INTERSECTIONS', '10'),
('NUM_INTERSECTIONS', '20'),
('NUM_INTERSECTIONS', '50'),
('NUM_INTERSECTIONS', '100'),
('missing_pattern', 'random'),
('missing_pattern', 'block'),
('missing_pattern', 'continuous'),
('missing_rate', '0.1'),
('missing_rate', '0.2'),
('missing_rate', '0.3'),
('missing_rate', '0.5');

-- 注意：默认管理员账户由 AdminBootstrap 类在应用启动时自动创建
-- 用户名：admin，密码：Admin@123（BCrypt加密），手机号：13800000000