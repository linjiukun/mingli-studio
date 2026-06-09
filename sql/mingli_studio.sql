-- ============================================================
-- 命理阁 (MingLi Studio) 数据库初始化脚本
-- 数据库: MariaDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS mingli_studio DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mingli_studio;

-- ============================================================
-- 1. 用户表 (sys_user)
-- ============================================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    real_name   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    gender      CHAR(1)      DEFAULT '2' COMMENT '性别(0男 1女 2未知)',
    birth_date  DATE         DEFAULT NULL COMMENT '出生日期',
    birth_hour  VARCHAR(10)  DEFAULT NULL COMMENT '出生时辰',
    birth_place VARCHAR(100) DEFAULT NULL COMMENT '出生地',
    email       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    avatar      VARCHAR(200) DEFAULT NULL COMMENT '头像URL',
    status      CHAR(1)      DEFAULT '0' COMMENT '状态(0正常 1停用)',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================================
-- 2. 八字命盘表 (fortune_birth_chart)
-- ============================================================
DROP TABLE IF EXISTS fortune_birth_chart;
CREATE TABLE fortune_birth_chart (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '命盘ID',
    user_id         BIGINT       NOT NULL COMMENT '用户ID',
    birth_date      DATE         NOT NULL COMMENT '出生日期',
    birth_hour      VARCHAR(10)  DEFAULT NULL COMMENT '出生时辰',
    gender          CHAR(1)      DEFAULT '2' COMMENT '性别',
    year_pillar     VARCHAR(20)  DEFAULT NULL COMMENT '年柱',
    month_pillar    VARCHAR(20)  DEFAULT NULL COMMENT '月柱',
    day_pillar      VARCHAR(20)  DEFAULT NULL COMMENT '日柱',
    hour_pillar     VARCHAR(20)  DEFAULT NULL COMMENT '时柱',
    heavenly_stems  VARCHAR(50)  DEFAULT NULL COMMENT '天干',
    earthly_branches VARCHAR(50) DEFAULT NULL COMMENT '地支',
    five_elements   TEXT         DEFAULT NULL COMMENT '五行分析',
    luck_gods       VARCHAR(100) DEFAULT NULL COMMENT '喜用神',
    unluck_gods     VARCHAR(100) DEFAULT NULL COMMENT '忌神',
    summary         TEXT         DEFAULT NULL COMMENT '命理总评',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='八字命盘表';

-- ============================================================
-- 3. 命理解读记录表 (fortune_reading)
-- ============================================================
DROP TABLE IF EXISTS fortune_reading;
CREATE TABLE fortune_reading (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '解读ID',
    user_id      BIGINT       NOT NULL COMMENT '用户ID',
    chart_id     BIGINT       DEFAULT NULL COMMENT '关联八字ID',
    reading_type VARCHAR(20)  DEFAULT NULL COMMENT '解读类型(general/wealth/marriage/career)',
    content      TEXT         DEFAULT NULL COMMENT '解读内容',
    score        INT          DEFAULT NULL COMMENT '评分(1-100)',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_chart_id (chart_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命理解读记录表';

-- ============================================================
-- 4. 每日运势表 (fortune_daily)
-- ============================================================
DROP TABLE IF EXISTS fortune_daily;
CREATE TABLE fortune_daily (
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '运势ID',
    user_id          BIGINT       NOT NULL COMMENT '用户ID',
    fortune_date     DATE         NOT NULL COMMENT '运势日期',
    overall_score    INT          DEFAULT 0 COMMENT '综合评分',
    wealth_score     INT          DEFAULT 0 COMMENT '财运评分',
    love_score       INT          DEFAULT 0 COMMENT '爱情评分',
    career_score     INT          DEFAULT 0 COMMENT '事业评分',
    health_score     INT          DEFAULT 0 COMMENT '健康评分',
    lucky_color      VARCHAR(50)  DEFAULT NULL COMMENT '幸运色',
    lucky_number     VARCHAR(20)  DEFAULT NULL COMMENT '幸运数字',
    lucky_direction  VARCHAR(50)  DEFAULT NULL COMMENT '吉方',
    avoid_direction  VARCHAR(50)  DEFAULT NULL COMMENT '忌方',
    advice           TEXT         DEFAULT NULL COMMENT '每日建议',
    create_time      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_date (user_id, fortune_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日运势表';

-- ============================================================
-- 5. 占卜记录表 (fortune_divination)
-- ============================================================
DROP TABLE IF EXISTS fortune_divination;
CREATE TABLE fortune_divination (
    id                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '占卜ID',
    user_id           BIGINT       NOT NULL COMMENT '用户ID',
    question          TEXT         DEFAULT NULL COMMENT '问题',
    divination_method VARCHAR(20)  DEFAULT NULL COMMENT '占卜方式(六爻/梅花易数)',
    hexagram_name     VARCHAR(50)  DEFAULT NULL COMMENT '卦名',
    hexagram_symbol   VARCHAR(20)  DEFAULT NULL COMMENT '卦象',
    interpretation    TEXT         DEFAULT NULL COMMENT '卦象解读',
    advice            TEXT         DEFAULT NULL COMMENT '建议',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='占卜记录表';

-- ============================================================
-- 初始数据
-- ============================================================

-- 插入管理员用户 (admin / admin123, BCrypt加密)
-- 注意: 密码使用 BCryptPasswordEncoder 加密，如需重置请重新生成
INSERT INTO sys_user (username, password, nickname, real_name, gender, status, create_time, update_time)
VALUES ('admin', '$2a$10$SgeopccMREhgmjfb0dL1nOemoMW06W.BzB6sTA9.gVakckYyuTJYa',
        '管理员', '系统管理员', '0', '0', NOW(), NOW());
