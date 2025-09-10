# DiffLight 全栈项目

一个基于深度学习的交通信号灯优化平台，专注于处理交通数据中的缺失模式，提供智能化的信号灯控制方案。

## 技术栈

### 后端
- **Spring Boot 3** - 企业级 Java 应用框架
- **Spring Security** - 安全认证框架
- **JWT** - 无状态认证令牌
- **Spring Data JPA** - 数据持久化
- **MySQL** - 关系型数据库
- **Maven** - 项目构建工具
- **Java 17** - 编程语言

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 现代化构建工具
- **Vue Router** - 单页应用路由
- **Pinia** - 状态管理
- **Axios** - HTTP 客户端

## 功能概览

### 用户功能
- **用户注册/登录** - 安全的用户认证系统
- **数据集管理** - 上传和下载交通数据集
- **参数设置** - 配置交通优化参数
- **结果可视化** - 查看优化结果图表
- **个人信息管理** - 管理用户资料

### 管理员功能
- **参数选项管理** - 动态配置系统参数选项
- **数据集审核** - 管理公共数据集
- **用户管理** - 查看和管理用户信息
- **结果图片上传** - 为参数组合上传对应的结果图片

### 特殊业务规则
系统内置针对特定城市和路网的优化规则：

- **杭州 + roadnet_4_4 + 16个交叉路口**
  - random_missing: 10%, 30%, 50%
  - kriging_missing: 6.25%, 12.5%, 18.75%

- **济南 + roadnet_3_4 + 16个交叉路口**
  - random_missing: 10%, 30%, 50%
  - kriging_missing: 8.33%, 16.67%, 25%

## 项目结构

```
traffic-system/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/difflight/
│   │   ├── entity/            # 实体类
│   │   ├── repository/        # 数据访问层
│   │   ├── controller/        # 控制器
│   │   ├── dto/              # 数据传输对象
│   │   ├── security/         # 安全配置
│   │   └── config/           # 配置类
│   ├── src/main/resources/
│   │   ├── application.yml   # 应用配置
│   │   └── data.sql          # 初始化数据
│   └── storage/              # 文件存储目录
├── frontend/                   # Vue 3 前端
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   ├── components/       # 通用组件
│   │   ├── stores/           # Pinia 状态管理
│   │   ├── api/              # API 服务
│   │   └── router/           # 路由配置
│   └── dist/                 # 构建输出
└── README.md
```

## 数据库表结构

- **users** - 用户表（账号、密码、手机号）
- **admins** - 管理员表（账号、密码、手机号）
- **datasets** - 数据集表（文件信息、上传时间、公共标识）
- **download_logs** - 下载日志表（用户、数据集、下载时间）
- **parameter_entries** - 参数条目表（交通文件、路网文件、交叉路口数、缺失模式、缺失率）
- **result_images** - 结果图片表（参数条目ID、图片类型、文件路径、标题）
- **param_options** - 参数选项表（类型、值、显示名称、排序）

## 运行说明

### 1. 数据库准备
```sql
-- 创建数据库
CREATE DATABASE difflight CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 后端运行
```bash
cd backend

# 修改 application.yml 中的数据库连接配置
# 设置环境变量或直接修改配置文件：
# DB_HOST=localhost
# DB_PORT=3306
# DB_NAME=difflight
# DB_USERNAME=your_username
# DB_PASSWORD=your_password

# 编译和运行
mvn clean package
java -jar target/difflight-0.0.1-SNAPSHOT.jar
```

后端服务将在 http://localhost:8080 启动

### 3. 前端运行
```bash
cd frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产构建
npm run build
```

前端服务将在 http://localhost:5173 启动

## 默认账号

系统预置了管理员账号，可直接使用：

- **用户名**: admin
- **密码**: Admin@123
- **手机号**: 13800000000

## 主要特性

### 认证与授权
- JWT 无状态认证
- 基于角色的访问控制（用户/管理员）
- 安全的密码加密存储

### 参数配置系统
- 五类参数选项：交通文件、路网文件、交叉路口数、缺失模式、缺失率
- 动态参数验证
- 管理员可实时增删改参数选项

### 文件管理
- 数据集上传/下载
- 结果图片管理
- 完整的下载日志追踪

### 响应式界面
- 适配移动端和桌面端
- 现代化 UI 设计
- 直观的用户体验

## 开发说明

### 后端 API
- **认证接口**: `/api/auth/*`
- **参数接口**: `/api/params/*`
- **管理员接口**: `/api/admin/*`
- **静态资源**: `/api/results/public/*`

### 前端路由
- `/` - 首页
- `/login` - 登录页
- `/register` - 注册页
- `/param-settings` - 参数设置
- `/dataset` - 数据集管理
- `/profile` - 个人信息
- `/admin/*` - 管理员功能（需要管理员权限）

### 环境配置
后端配置文件 `application.yml` 使用环境变量占位符，便于部署时配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:difflight}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
```

## 注意事项

1. **首次启动**：系统会自动创建数据库表并插入初始化数据
2. **文件存储**：上传的文件保存在 `backend/storage/` 目录下
3. **跨域配置**：已配置允许前端跨域访问
4. **安全性**：请在生产环境中修改默认的 JWT 密钥和管理员密码

## 许可证

本项目仅供学习和研究使用。
