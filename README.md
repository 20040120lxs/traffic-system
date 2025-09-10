# DiffLight 交通系统

一个专业的交通数据处理和分析系统，提供数据集管理、参数配置、结果分析等功能。

## 技术栈

**前端**：Vue 3 + Vite + Vue Router + Pinia + Element Plus + Axios  
**后端**：Spring Boot 3 + Spring Security + JWT + Spring Data JPA  
**数据库**：MySQL  

## 核心功能

### 用户功能
- **用户注册/登录**：支持普通用户注册，账号密码登录
- **数据集管理**：上传、查看私有数据集，下载公开数据集
- **参数配置**：灵活的参数设置，支持多种交通分析场景
- **结果查看**：直观的图表展示和分析结果

### 管理员功能  
- **管理员登录**：独立的管理员认证系统
- **数据集管理**：查看所有用户数据集，设置公开状态
- **用户管理**：查看和管理用户信息
- **参数选项管理**：维护系统参数选项配置

## 项目结构

```
traffic-system/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/difflight/
│   │       ├── config/      # 配置类（安全、管理员初始化）
│   │       ├── controller/  # 控制器
│   │       ├── entity/      # 实体类
│   │       ├── repository/  # 数据访问层
│   │       ├── security/    # 安全相关（JWT）
│   │       └── service/     # 业务逻辑层
│   ├── src/main/resources/
│   │   ├── application.yml  # 应用配置
│   │   ├── schema.sql      # 数据库表结构
│   │   └── data.sql        # 初始数据
│   └── storage/            # 文件存储目录
├── frontend/               # Vue 3 前端
│   ├── src/
│   │   ├── api/           # API 接口
│   │   ├── pages/         # 页面组件
│   │   ├── store/         # Pinia 状态管理
│   │   └── components/    # 公共组件
│   └── dist/              # 构建输出
└── README.md
```

## 数据库设计

- **users**：用户信息表（账号、密码、手机号）
- **admins**：管理员信息表（账号、密码、手机号、角色）
- **datasets**：数据集信息表（文件信息、所有者、公开状态）
- **download_logs**：下载日志表（用户、数据集、下载时间）
- **param_options**：参数选项表（参数类型、值）

## 快速开始

### 1. 数据库准备
```sql
CREATE DATABASE difflight CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 后端启动
```bash
cd backend
# 配置数据库连接（application.yml）
mvn clean package
java -jar target/difflight-0.0.1-SNAPSHOT.jar
```

### 3. 前端启动
```bash
cd frontend
npm install
npm run dev
# 访问 http://localhost:5173
```

## 默认账号

**管理员（自动创建）**
- 用户名：`admin`
- 密码：`Admin@123`
- 手机号：`13800000000`

## 重要特性

### ✅ 管理员登录修复
- **AdminBootstrap**：应用启动时自动检查并创建默认管理员账户
- **密码加密**：使用 BCrypt 算法安全加密密码
- **权限控制**：精确的角色权限验证（`hasAuthority("ADMIN")`）
- **JWT 认证**：无状态认证，角色信息精确为 "USER" 或 "ADMIN"

### ✅ 数据集上传功能
- **真实实现**：替换桩实现，提供完整的文件上传功能
- **安全存储**：文件保存到 `backend/storage/datasets` 目录
- **文件管理**：UUID 重命名防重复，自动创建存储目录
- **前端对接**：拖拽上传界面，实时状态反馈
- **权限控制**：仅登录用户可上传，管理员可设置公开状态

### 安全配置
- `/api/auth/**`：认证接口，允许匿名访问
- `/api/admin/**`：管理员接口，需要 ADMIN 权限
- `/api/datasets/upload`：数据集上传，需要登录
- 其他 API：需要认证

### 配置说明
- **文件上传**：最大 50MB，支持 multipart/form-data
- **存储路径**：`app.storage.datasets` 和 `app.storage.results` 可配置
- **CORS**：支持前端跨域访问
- **数据库**：支持环境变量配置，便于部署

## 使用说明

1. **首次启动**：系统自动建表并创建默认管理员账户
2. **用户注册**：普通用户可注册账号并上传私有数据集
3. **管理员登录**：使用默认账号登录管理后台
4. **数据集上传**：支持拖拽上传，文件自动保存和记录
5. **权限管理**：管理员可将私有数据集设置为公开

## 开发说明

- 所有注释和文档使用中文
- 使用环境变量占位符，便于生产部署
- 遵循 RESTful API 设计规范
- 前端使用 Element Plus 组件库
- 后端使用 Spring Boot 最佳实践
