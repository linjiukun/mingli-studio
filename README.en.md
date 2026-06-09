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

MingLi Studio is a full-stack fortune analysis system integrating Chinese BaZi (Eight Characters) astrology, fortune reading, daily horoscopes, I-Ching divination, and Western zodiac analysis. Built with a separated front-end and back-end architecture, it features a complete built-in fortune-telling algorithm engine — all calculations are performed server-side with zero reliance on third-party APIs.

### ✨ Feature Modules

| Module | Description |
|--------|-------------|
| ☯ **BaZi Fortune** | Four Pillars calculation (Year/Month/Day/Hour), Five Elements analysis, Lucky/Unlucky Element recommendation, comprehensive reading (449-line core algorithm) |
| 📜 **Fortune Reading** | Multi-dimensional analysis covering wealth, marriage, career, and general fortune, with structured interpretation text |
| ☀️ **Daily Fortune** | Personalized daily fortune generated via date + user ID pseudo-random algorithm, covering overall/wealth/love/career/health scores |
| 🔮 **Divination** | Full 64-hexagram mapping + random casting, with hexagram names, symbols, and detailed interpretation suggestions |
| ♈ **Zodiac** | Complete Western zodiac reference (date range, element, ruling planet, personality traits), daily/weekly/monthly horoscopes, zodiac compatibility analysis |

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
| UI | Element Plus 2.6 + Custom Dark Esoteric Theme |
| Router | Vue Router 4 (Lazy Loading) |
| State Management | Pinia |
| HTTP | Axios (Request/Response Interceptors) |
| Build | Vite 5 |

### Project Structure

```
mingli-studio/
├── mingli-admin/          # Entry point (Spring Boot Application)
├── mingli-common/         # Common module (Result, JwtUtil, BaseEntity)
├── mingli-system/         # System module (SysUser, Mapper, Service)
├── mingli-fortune/        # Fortune core module (BaZi/Horoscope/Divination/Zodiac)
│   ├── controller/        # REST API controllers
│   ├── service/           # Business logic
│   ├── mapper/            # MyBatis data access
│   ├── domain/            # Entity classes
│   └── utils/             # Core algorithms (BaziCalculator, ZodiacCalculator)
├── mingli-framework/      # Framework config (CORS, MyBatis Config, Auth Interceptor)
├── mingli-generator/      # Code generator (skeleton)
├── mingli-web/            # Frontend (Vue 3 + Vite)
│   └── src/
│       ├── api/           # Axios API wrappers
│       ├── views/         # Page components (8)
│       ├── layout/        # Layout (Sidebar/Navbar)
│       ├── stores/        # Pinia state management
│       ├── router/        # Route configuration
│       └── styles/        # Dark esoteric theme (363 lines)
└── sql/                   # Database initialization scripts
```

---

## 🚀 Quick Start

### Prerequisites

- **JDK 8+** (Temurin JDK 8 recommended)
- **Maven 3.6+**
- **Node.js 16+**
- **MariaDB 10.5+** (or MySQL 5.7+)
- **WSL / Linux / macOS / Windows**

### 1️⃣ Database Setup

```bash
# Create database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS mingli_studio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Import schema + initial data
mysql -u root -p mingli_studio < sql/mingli_studio.sql
```

### 2️⃣ Database Connection Setup

Since `application-dev.yml` is excluded via `.gitignore`, you must **create it yourself** after cloning:

**Create** `mingli-admin/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    druid:
      url: jdbc:mariadb://localhost:3306/mingli_studio?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: your_username
      password: your_password
```

> **Note**: The above is a template — replace `your_username` and `your_password` with your actual database credentials.

### 3️⃣ Start Backend

```bash
# Compile and install dependencies
mvn clean install -DskipTests

# Start server (default port: 8088)
mvn spring-boot:run -pl mingli-admin
```

### 4️⃣ Start Frontend

```bash
cd mingli-web
npm install
npm run dev
```

### 5️⃣ Access

Open `http://localhost:3000` in your browser and log in with the default account:

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

### BaZi (Eight Characters)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/bazi/calculate` | Calculate BaZi chart |
| GET | `/api/bazi/my-chart` | Get my birth chart |
| GET | `/api/bazi/history` | Historical calculation records |

### Fortune Reading

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/fortune/reading` | Request fortune reading |
| GET | `/api/fortune/readings` | Historical reading records |

### Daily Fortune

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/daily/today` | Today's fortune |
| GET | `/api/daily/range` | Fortune for a date range |

### Divination

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/divination/ask` | Cast a divination |

### Zodiac ✨

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/zodiac/my-sign` | My zodiac sign |
| GET | `/api/zodiac/all-signs` | All zodiac signs list |
| GET | `/api/zodiac/sign/{index}` | Zodiac sign detail (0-11) |
| GET | `/api/zodiac/calculate` | Calculate zodiac from birthday |
| GET | `/api/zodiac/daily` | Daily horoscope |
| GET | `/api/zodiac/weekly` | Weekly horoscope |
| GET | `/api/zodiac/monthly` | Monthly horoscope |
| GET | `/api/zodiac/compatibility` | Zodiac compatibility analysis |

---

## 🧮 Core Algorithms

### BaZi Fortune (BaziCalculator.java)

- **Four Pillars Calculation**: Heavenly Stems & Earthly Branches for Year, Month, Day, and Hour pillars
- **Five Elements Analysis**: Distribution statistics and strength assessment for Metal, Wood, Water, Fire, Earth
- **Lucky/Unlucky Elements**: Intelligent recommendations based on elemental balance
- **Comprehensive Reading**: Structured fortune interpretation text generation

### Western Zodiac (ZodiacCalculator.java)

- **Sign Determination**: Precise zodiac calculation based on Gregorian month/day, with cross-year handling, invalid-date validation, and boundary tests
- **Daily Horoscope**: Pseudo-random algorithm based on sign + date, ensuring consistency for same sign on same date
- **Weekly/Monthly Horoscope**: Multi-template rotation; weekly horoscopes remain stable within the same calendar week, monthly horoscopes remain stable within the same month
- **Compatibility Analysis**: 12×12 compatibility matrix with scores, ratings, and suggestions
- **64 Hexagrams Divination**: Complete hexagram mapping + random casting + interpretation suggestions

---

## 🎨 Frontend Theme

Dark esoteric style with custom CSS variable system:

```css
--bg-primary: #0f0f1a;     /* Deep night sky */
--bg-secondary: #1a1a2e;   /* Purplish-black background */
--gold: #c9a84c;           /* Gold primary color */
--text-gold: #d4af37;      /* Gold text */
--text-muted: #a89080;     /* Muted gold auxiliary text */
```

---

## 📄 Database Schema

| Table | Description |
|-------|-------------|
| `sys_user` | Users (with birth date/time/gender) |
| `fortune_birth_chart` | BaZi birth charts |
| `fortune_reading` | Fortune reading records |
| `fortune_daily` | Daily fortune records |
| `fortune_divination` | Divination records |

---

## 🛠 Development Guide

### Adding New Features

1. **Backend**: Add Controller → Service → Mapper in `mingli-fortune` module
2. **Frontend**: Add page in `mingli-web/src/views/` → API wrapper in `api/` → route in `router/` → navigation item in `layout/Sidebar.vue`
3. **Database**: Add table structure in `sql/mingli_studio.sql`

### Troubleshooting

- **Port in use**: `fuser -k 8088/tcp` to free the port
- **Mapper binding error**: Verify `mapper-locations: classpath*:mapper/**/*.xml`
- **Build failure**: Run `mvn clean install -DskipTests` to rebuild

---

## 📜 License

MIT License © 2026 linjiukun
