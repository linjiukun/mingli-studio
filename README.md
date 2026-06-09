# 命理阁 (MingLi Studio)

> **观天之道，执天之行** —— 一个基于中国传统命理学的全栈运势分析系统。

![Tech Stack](https://img.shields.io/badge/Backend-Spring%20Boot%202.6.15-brightgreen)
![Tech Stack](https://img.shields.io/badge/Frontend-Vue%203.4%20%2B%20Element%20Plus-gold)
![Tech Stack](https://img.shields.io/badge/Database-MariaDB-blue)
![Tech Stack](https://img.shields.io/badge/Build-Maven%203.9.5-red)

---

## 📋 项目简介

命理阁是一个集八字算命、命理解读、每日运势、占卜问卦、十二星座为一体的全栈命理运势系统。采用前后端分离架构，内置完整的命理算法引擎，所有运算在服务端完成，不依赖第三方 API。

### ✨ 功能模块

| 模块 | 功能 |
|------|------|
| ☯ **八字算命** | 基于出生年月日时计算四柱八字、五行分析、喜用神判断、命理总评（内置 449 行核心算法） |
| 📜 **命理解读** | 财运、婚姻、事业、综合等多维度命理分析，带结构化解读文本 |
| ☀️ **每日运势** | 基于日期+用户 ID 伪随机生成个性化每日运势，含综合/财运/爱情/事业/健康评分 |
| 🔮 **占卜问卦** | 64 卦完整映射 + 随机起卦，含卦象名称、符号、详细解卦建议 |
| ♈ **十二星座** | 12 星座大全（日期范围/元素/主宰星/性格特征）、每日/周/月运势、星座配对分析 |

---

## 🏗 技术架构

### 后端技术栈

| 组件 | 技术选型 | 说明 |
|------|----------|------|
| 框架 | Spring Boot 2.6.15 | 稳定版本，兼容 JDK 8 |
| ORM | MyBatis + PageHelper | XML 映射 + 自动分页 |
| 安全 | JWT (jjwt 0.12.3) + 自定义拦截器 | 无状态鉴权，无需 Redis |
| 数据库 | MariaDB + Druid 连接池 | 高性能连接池 + SQL 监控 |
| 工具 | Hutool 5.8, Lombok | 简化开发 |
| 构建 | Maven 3.9.5 | 多模块管理 |

### 前端技术栈

| 组件 | 技术选型 |
|------|----------|
| 框架 | Vue 3.4 (Composition API) |
| UI | Element Plus 2.6 + 自定义暗色玄学主题 |
| 路由 | Vue Router 4 (懒加载) |
| 状态管理 | Pinia |
| HTTP | Axios (请求/响应拦截器) |
| 构建 | Vite 5 |

### 项目结构

```
mingli-studio/
├── mingli-admin/          # 入口模块 (Spring Boot 启动类)
├── mingli-common/         # 公共模块 (Result, JwtUtil, BaseEntity)
├── mingli-system/         # 系统模块 (SysUser, Mapper, Service)
├── mingli-fortune/        # 命理核心模块 (四柱八字/运势/占卜/星座)
│   ├── controller/        # REST API 控制器
│   ├── service/           # 业务逻辑
│   ├── mapper/            # MyBatis 数据访问
│   ├── domain/            # 实体类
│   └── utils/             # 核心算法 (BaziCalculator, ZodiacCalculator)
├── mingli-framework/      # 框架配置 (CORS, MyBatis Config, Auth Interceptor)
├── mingli-generator/      # 代码生成器 (骨架)
├── mingli-web/            # 前端 (Vue 3 + Vite)
│   └── src/
│       ├── api/           # Axios API 封装
│       ├── views/         # 页面组件 (8 个)
│       ├── layout/        # 布局 (侧边栏/顶栏)
│       ├── stores/        # Pinia 状态管理
│       ├── router/        # 路由配置
│       └── styles/        # 暗色玄学主题 (363 行)
└── sql/                   # 数据库初始化脚本
```

---

## 🚀 快速开始

### 环境要求

- **JDK 8+** (推荐 Temurin JDK 8)
- **Maven 3.6+**
- **Node.js 16+**
- **MariaDB 10.5+** (或 MySQL 5.7+)
- **WSL / Linux / macOS / Windows**

### 1️⃣ 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS mingli_studio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入表结构 + 初始数据
mysql -u root -p mingli_studio < sql/mingli_studio.sql
```

### 2️⃣ 配置数据库连接

编辑 `mingli-admin/src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mariadb://localhost:3306/mingli_studio?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: your_username
      password: your_password
```

> **注意**：`application-dev.yml` 已在 `.gitignore` 中排除，不会提交到 Git，避免泄露密码。

### 3️⃣ 启动后端

```bash
# 编译并安装依赖
mvn clean install -DskipTests

# 启动服务 (默认端口 8088)
mvn spring-boot:run -pl mingli-admin
```

### 4️⃣ 启动前端

```bash
cd mingli-web
npm install
npm run dev
```

### 5️⃣ 访问

浏览器打开 `http://localhost:3000`，使用默认账号登录：

| 账号 | 密码 |
|------|------|
| `admin` | `admin123` |

---

## 📡 API 接口总览

### 认证模块

| Method | Path | 说明 |
|--------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 用户注册 |

### 八字测算

| Method | Path | 说明 |
|--------|------|------|
| POST | `/api/bazi/calculate` | 计算八字命盘 |
| GET | `/api/bazi/my-chart` | 获取我的命盘 |
| GET | `/api/bazi/history` | 历史测算记录 |

### 命理解读

| Method | Path | 说明 |
|--------|------|------|
| POST | `/api/fortune/reading` | 请求命理解读 |
| GET | `/api/fortune/readings` | 历史解读记录 |

### 每日运势

| Method | Path | 说明 |
|--------|------|------|
| GET | `/api/daily/today` | 今日运势 |
| GET | `/api/daily/range` | 指定日期范围运势 |

### 占卜问卦

| Method | Path | 说明 |
|--------|------|------|
| POST | `/api/divination/ask` | 占卜问卦 |

### 星座运势 ✨

| Method | Path | 说明 |
|--------|------|------|
| GET | `/api/zodiac/my-sign` | 我的星座 |
| GET | `/api/zodiac/all-signs` | 所有星座列表 |
| GET | `/api/zodiac/sign/{index}` | 星座详情 (0-11) |
| GET | `/api/zodiac/calculate` | 按生日算星座 |
| GET | `/api/zodiac/daily` | 每日运势 |
| GET | `/api/zodiac/weekly` | 周运势 |
| GET | `/api/zodiac/monthly` | 月运势 |
| GET | `/api/zodiac/compatibility` | 星座配对分析 |

---

## 🧮 核心算法

### 八字算命 (BaziCalculator.java)

- **四柱推算**：年柱、月柱、日柱、时柱的天干地支计算
- **五行分析**：金木水火土分布统计与旺衰判断
- **喜用神/忌神**：根据五行平衡智能推荐
- **命理总评**：结构化命理解读文本生成

### 十二星座 (ZodiacCalculator.java)

- **星座判定**：基于公历月日的精确星座计算（含跨年处理）
- **每日运势**：基于星座+日期的伪随机算法，保证同星座同日期运势一致
- **每周/月运势**：多模板轮换，定期更新
- **配对分析**：12×12 兼容性矩阵，含评分/等级/建议
- **64 卦占卜**：完整卦象映射 + 随机起卦 + 解卦建议

---

## 🎨 前端主题

暗色玄学风格，自定义 CSS 变量体系：

```css
--bg-primary: #0f0f1a;     /* 深邃夜空 */
--bg-secondary: #1a1a2e;   /* 紫黑底色 */
--gold: #c9a84c;           /* 金色主色调 */
--text-gold: #d4af37;      /* 金色文字 */
--text-muted: #a89080;     /* 暗金色辅助文字 */
```

---

## 📄 数据库表结构

| 表名 | 说明 |
|------|------|
| `sys_user` | 用户表（含出生日期/时辰/性别） |
| `fortune_birth_chart` | 八字命盘表 |
| `fortune_reading` | 命理解读记录 |
| `fortune_daily` | 每日运势记录 |
| `fortune_divination` | 占卜问卦记录 |

---

## 🛠 开发指南

### 添加新功能

1. **后端**：在 `mingli-fortune` 模块新增 Controller → Service → Mapper
2. **前端**：在 `mingli-web/src/views/` 新增页面 → `api/` 新增接口 → `router/` 注册路由 → `layout/Sidebar.vue` 添加导航
3. **数据库**：在 `sql/mingli_studio.sql` 添加表结构

### 常见问题

- **端口占用**：`fuser -k 8088/tcp` 释放端口
- **Mapper 绑定错误**：检查 `mapper-locations: classpath*:mapper/**/*.xml`
- **编译失败**：`mvn clean install -DskipTests` 重新构建

---

## 📜 许可

MIT License © 2026 linjiukun
