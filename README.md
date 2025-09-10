# DiffLight 全栈项目

前端：Vue 3 + Vite + Vue Router + Pinia  
后端：Spring Boot 3 + Spring Security + JWT + Spring Data JPA  
数据库：MySQL

功能概览
- 首页：标题 DiffLight，正文介绍占位
- 导航：首页、数据集上传/下载、参数设置、个人信息管理、登录/注册
- 注册：账号、密码、确认密码、手机号
- 登录：账号、密码；支持管理员登录（单独表），登录后导航新增「数据集管理」「用户信息管理」「参数选项管理」
- 数据集上传/下载：用户上传自己的数据集，下载公共数据集；下载会记录下载日志
- 参数设置：参数选项从后端数据库获取（管理员可增删改选项）。设置后返回与之对应的图片（轮播图），可动态添加图片，显示图片标题与所选参数表格
- 管理端：
  - 数据集管理：查看用户上传的数据集（及上传时间），管理员也可上传公共数据集供下载
  - 用户信息管理：查看/修改所有用户的账号、密码、手机号
  - 参数选项管理：增删改五类参数选项（traffic_file、roadnet_file、NUM_INTERSECTIONS、missing_pattern、missing_rate）

数据库表
- 用户表（users：账号、密码、手机号码）
- 管理员表（admins：账号、密码、手机号码）
- 数据集表（datasets：数据集id，账号，上传时间，文件名/路径，是否公共）
- 数据集下载信息表（download_logs：序号、账号，数据集id，下载时间）
- 参数表（parameters：序号，交通文件名，路网文件名、交叉路口数、缺失模式、缺失率，更新时间）
- 结果表（results：参数表序号，填补结果图、加噪过程图、去噪过程图、更新时间）
- 参数选项表（param_options：类型、值、更新时间）

运行
1) MySQL 创建数据库 difflight，并修改 backend/src/main/resources/application.yml 连接配置
2) 后端
   - cd backend
   - mvn clean package
   - java -jar target/difflight-0.0.1-SNAPSHOT.jar
3) 前端
   - cd frontend
   - npm install
   - npm run dev
   - 打开 http://localhost:5173

默认账号
- 管理员（预置）：admin / Admin@123，手机号：13800000000

说明
- 首次启动会自动建表并预置管理员与参数选项（data.sql）
- 管理员在「参数选项管理」中增删改选项后，用户端「参数设置」页面的下拉框会自动同步
- 管理员通过「参数设置」页面下方的管理区上传与参数组合对应的图片，用户点击确定后将看到轮播图
