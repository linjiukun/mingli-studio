# MingLi Studio (命理阁)

[中文](README.md) | [English](README.en.md)

![Tech Stack](https://img.shields.io/badge/Backend-Spring%20Boot%202.6.15-brightgreen)
![Tech Stack](https://img.shields.io/badge/Frontend-Vue%203.4%20%2B%20Element%20Plus-gold)
![Tech Stack](https://img.shields.io/badge/Database-MariaDB-blue)
![Tech Stack](https://img.shields.io/badge/Build-Maven%203.9.5-red)


> **"Observe the Way of Heaven, Execute the Will of Heaven"** — A full-stack fortune analysis system based on traditional Chinese metaphysics.

> ⚠️ **Important**: `application-dev.yml` (database connection configuration) is excluded via `.gitignore`. **You must create this file yourself after cloning.** See the "Database Connection Setup" section below.

---

## 📋 Introduction

MingLi Studio is a full-stack fortune analysis system integrating Chinese BaZi (Eight Characters) astrology, fortune reading, daily horoscopes, I-Ching divination, Western zodiac analysis, metaphysics community, and fortune teller consultation. Built with a separated front-end and back-end architecture, it features a complete built-in fortune-telling algorithm engine — all calculations are performed server-side with zero reliance on third-party APIs.

### ✨ Feature Modules

| Module | Description |
|--------|-------------|
| ☯ **BaZi Fortune** | Four Pillars calculation (Year/Month/Day/Hour), Five Elements analysis, Lucky/Unlucky Element recommendation, comprehensive reading (449-line core algorithm) |
| 📜 **Fortune Reading** | Multi-dimensional analysis covering wealth, marriage, career, and general fortune, with structured interpretation text |
| ☀️ **Daily Fortune** | Personalized daily fortune generated via date + user ID pseudo-random algorithm, covering overall/wealth/love/career/health scores |
| 🔮 **Divination** | Full 64-hexagram mapping + random casting, with hexagram names, symbols, and detailed interpretation suggestions |
| ♈ **Zodiac** | Complete Western zodiac reference (date range, element, ruling planet, personality traits), daily/weekly/monthly horoscopes, zodiac compatibility analysis |
| 💬 **Metaphysics Community** 🆕 | Article publishing, category filtering, search, favorites, comments, likes with 8 categories |
| 👨‍🏫 **Fortune Tellers** 🆕 | Fortune teller listing, application, certification, rating system |
| 🔔 **Notifications** 🆕 | Real-time notifications for comments, likes, favorites, follows with read/unread management |
| 👥 **User Following** 🆕 | Follow system between users, building fan base |
| 🏠 **User Profile** 🆕 | Display user's articles, favorites, followers, following lists |

---

## 🏗 Tech Stack

### Backend

| Component | Technology | Notes |
|-----------|-----------|-------|
| Framework | Spring Boot 2.6.15 | Stable release, JDK 8 compatible |
| ORM | MyBatis + PageHelper | XML mapping + auto-pagination |
| Security | JWT (jjwt 0.12.3) + Custom Interceptor | Stateless auth, no Redis required |
| Database | MariaDB + Druid Connection Pool | High-performance pool + SQL monitoring |
| Utilities | Hutool 5.8, Lombok | Productivity boost |
| Build | Maven 3.9.5 | Multi-module management |

### Frontend

| Component | Technology |
|-----------|-----------|
| Framework | Vue 3.4 (Composition API) |
| UI | Element Plus 2.6 + Custom Dark Mystical Theme |
| Router | Vue Router 4 (Lazy loading) |
| State Management | Pinia |
| HTTP | Axios (Request/Response interceptors) |
| Build | Vite 5 |

### Project Structure

```
mingli-studio/
├── mingli-admin/          # Entry module (Spring Boot main class)
├── mingli-common/         # Common module (Result, JwtUtil, BaseEntity)
├── mingli-system/         # System module (SysUser, Mapper, Service)
├── mingli-fortune/        # Core fortune module (BaZi/Fortune/Divination/Zodiac/Community)
│   ├── controller/        # REST API controllers (including community/tellers)
│   ├── service/           # Business logic
│   ├── mapper/            # MyBatis data access
│   ├── domain/            # Entity classes (including community/tellers)
│   └── utils/             # Core algorithms (BaziCalculator, ZodiacCalculator)
├── mingli-framework/      # Framework config (CORS, MyBatis Config, Auth Interceptor)
├── mingli-generator/      # Code generator (skeleton)
├── mingli-web/            # Frontend (Vue 3 + Vite)
│   └── src/
│       ├── api/           # Axios API wrappers (including community/tellers)
│       ├── views/         # Page components (14 total)
│       ├── layout/        # Layout (sidebar/navbar)
│       ├── stores/        # Pinia state management
│       ├── router/        # Route configuration
│       └── styles/        # Dark mystical theme (363 lines)
└── sql/                   # Database initialization scripts
```

---

## 🚀 Quick Start

### Prerequisites

- **JDK 8+** (Recommended: Temurin JDK 8)
- **Maven 3.6+**
- **Node.js 16+**
- **MariaDB 10.5+** (or MySQL 5.7+)
- **WSL / Linux / macOS / Windows**

### 1️⃣ Database Initialization

```bash
# Create database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS mingli_studio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Import table structure + initial data
mysql -u root -p mingli_studio < sql/mingli_studio.sql

# Import community module tables (v1.1.0 new)
mysql -u root -p mingli_studio < sql/mingli_community.sql

# Import social module tables (v1.2.0 new)
mysql -u root -p mingli_studio < sql/mingli_social.sql
```

### 2️⃣ Configure Database Connection

Since `application-dev.yml` is excluded via `.gitignore`, you need to **create it yourself** after cloning:

**Create** `mingli-admin/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mariadb://localhost:3306/mingli_studio?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: your_username
      password: your_password
```

> **Note**: The above is an example. Replace `your_username` and `your_password` with your actual database credentials.

### 3️⃣ Start Backend

```bash
# Compile and install dependencies
mvn clean install -DskipTests

# Start service (default port 8088)
mvn spring-boot:run -pl mingli-admin
```

### 4️⃣ Start Frontend

```bash
cd mingli-web
npm install
npm run dev
```

### 5️⃣ Access

Open `http://localhost:3000` in your browser and login with the default account:

| Username | Password |
|----------|----------|
| `admin` | `admin123` |

---

## 📡 API Overview

### Authentication

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/register` | User registration |

### BaZi Fortune

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/bazi/calculate` | Calculate BaZi chart |
| GET | `/api/bazi/my-chart` | Get my chart |
| GET | `/api/bazi/history` | History records |

### Fortune Reading

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/fortune/reading` | Request fortune reading |
| GET | `/api/fortune/readings` | History readings |

### Daily Fortune

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/daily/today` | Today's fortune |
| GET | `/api/daily/range` | Date range fortune |

### Divination

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/divination/ask` | Ask divination |

### Zodiac ✨

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/zodiac/my-sign` | My zodiac sign |
| GET | `/api/zodiac/all-signs` | All zodiac signs |
| GET | `/api/zodiac/sign/{index}` | Sign details (0-11) |
| GET | `/api/zodiac/calculate` | Calculate by birthday |
| GET | `/api/zodiac/daily` | Daily horoscope |
| GET | `/api/zodiac/weekly` | Weekly horoscope |
| GET | `/api/zodiac/monthly` | Monthly horoscope |
| GET | `/api/zodiac/compatibility` | Compatibility analysis |

### Metaphysics Community 💬 🆕

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/community/articles` | Article list (with category filter, keyword search) |
| GET | `/api/community/articles/{id}` | Article details |
| POST | `/api/community/articles` | Publish article |
| PUT | `/api/community/articles/{id}` | Update article |
| DELETE | `/api/community/articles/{id}` | Delete article |
| POST | `/api/community/articles/{id}/favorite` | Favorite article |
| DELETE | `/api/community/articles/{id}/favorite` | Unfavorite article |
| GET | `/api/community/articles/{id}/comments` | Get article comments |
| POST | `/api/community/articles/{id}/comments` | Post comment |
| DELETE | `/api/community/comments/{id}` | Delete comment |
| POST | `/api/community/comments/{id}/like` | Like comment |
| GET | `/api/community/categories` | Get category list |

### Fortune Tellers 👨‍🏫 🆕

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/consultants` | Certified fortune teller list |
| GET | `/api/consultants/{id}` | Fortune teller details |
| GET | `/api/consultants/my` | Get current user's teller info |
| POST | `/api/consultants/apply` | Apply to become a fortune teller |
| PUT | `/api/consultants/{id}` | Update teller information |

### User Social 👥 🆕

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/social/follow/{id}` | Follow user |
| DELETE | `/api/social/follow/{id}` | Unfollow user |
| GET | `/api/social/following` | Get following list |
| GET | `/api/social/followers` | Get followers list |
| GET | `/api/social/counts` | Get follow/follower counts |

### Notifications 🔔 🆕

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/notification/list` | Notification list (with type filter) |
| GET | `/api/notification/unread/count` | Unread notification count |
| PUT | `/api/notification/{id}/read` | Mark notification as read |
| PUT | `/api/notification/read/all` | Mark all notifications as read |

---

## 🧮 Core Algorithms

### BaZi Fortune (BaziCalculator.java)

- **Four Pillars Calculation**: Heavenly Stems and Earthly Branches for Year/Month/Day/Hour pillars
- **Five Elements Analysis**: Metal/Wood/Water/Fire/Earth distribution and strength assessment
- **Lucky/Unlucky Elements**: Intelligent recommendation based on Five Elements balance
- **Comprehensive Reading**: Structured fortune interpretation text generation

### Western Zodiac (ZodiacCalculator.java)

- **Sign Determination**: Precise zodiac calculation based on Gregorian month/day (including cross-year handling, invalid date validation, and boundary testing)
- **Daily Horoscope**: Pseudo-random algorithm based on sign + date, ensuring consistent results for same sign on same date
- **Weekly/Monthly Horoscope**: Multiple template rotation, weekly horoscope stable by natural week, monthly horoscope stable by month
- **Compatibility Analysis**: 12×12 compatibility matrix with scores/levels/suggestions
- **64 Hexagrams Divination**: Complete hexagram mapping + random casting + interpretation suggestions

---

## 🎨 Frontend Theme

Dark mystical style with custom CSS variable system:

```css
--bg-primary: #0f0f1a;     /* Deep night sky */
--bg-secondary: #1a1a2e;   /* Purple-black background */
--gold: #c9a84c;           /* Gold primary color */
--text-gold: #d4af37;      /* Gold text */
--text-muted: #a89080;     /* Muted gold auxiliary text */
```

---

## 📄 Database Tables

| Table | Description |
|-------|-------------|
| `sys_user` | User table (with birth date/hour/gender) |
| `fortune_birth_chart` | BaZi chart table |
| `fortune_reading` | Fortune reading records |
| `fortune_daily` | Daily fortune records |
| `fortune_divination` | Divination records |
| `community_article` | Metaphysics article table 🆕 |
| `community_category` | Article category table 🆕 |
| `community_favorite` | User favorite table 🆕 |
| `community_comment` | Article comment table 🆕 |
| `fortune_consultant` | Fortune teller table 🆕 |
| `sys_user_follow` | User follow table 🆕 |
| `sys_notification` | Notification table 🆕 |
| `sys_user_activity` | User activity table 🆕 |
| `sys_user_stats` | User statistics table 🆕 |

---

## 🛠 Development Guide

### Adding New Features

1. **Backend**: Add Controller → Service → Mapper in `mingli-fortune` module
2. **Frontend**: Add page in `mingli-web/src/views/` → Add API in `api/` → Register route in `router/` → Add navigation in `layout/Sidebar.vue`
3. **Database**: Add table structure script in `sql/` directory

### Common Issues

- **Port in use**: `fuser -k 8088/tcp` to release port
- **Mapper binding error**: Check `mapper-locations: classpath*:mapper/**/*.xml`
- **Build failure**: `mvn clean install -DskipTests` to rebuild

---

## 📝 Changelog

### v1.2.0 (2026-06-13)

**🌟 New Features**
- 🔔 **Notifications**: Real-time notifications for comments, likes, favorites, follows with read/unread management
- 👥 **User Following**: Follow system between users, building fan base
- 🏠 **User Profile**: Display user's articles, favorites, followers, following lists
- 📊 **User Statistics**: Article count, follower count, likes received, etc.

**📦 New Files**
- Database: 4 new tables (`mingli_social.sql`)
- Backend: 16 Java files (4 entities + 4 mappers + 4 XML + 2 services + 2 controllers)
- Frontend: 2 page components + 2 API wrappers

**🔧 Improvements**
- Sidebar grouped display with notification entry
- Real-time notification badge count

### v1.1.0 (2026-06-13)

**🌟 New Features**
- 💬 **Metaphysics Community**: Article publishing, category filtering, search, favorites, comments, likes
- 👨‍🏫 **Fortune Tellers**: Fortune teller listing, application, certification, rating system
- 📖 **Article Details**: Rich text content display, comment section, favorite functionality

**📦 New Files**
- Database: 5 new tables (`mingli_community.sql`)
- Backend: 15 Java files (5 entities + 5 mappers + 5 services)
- Frontend: 3 page components + 2 API wrappers

**🔧 Improvements**
- Updated sidebar navigation with community and teller entries
- Added quick access shortcuts on dashboard

### v1.0.0 (2026-06-09)

- Initial release
- BaZi fortune, fortune reading, daily fortune, divination, zodiac

---

## 📜 License

MIT License © 2026 linjiukun

---