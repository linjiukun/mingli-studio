-- ============================================================
-- 命理阁社交模块数据库脚本 (v1.2.0)
-- 数据库: MariaDB
-- ============================================================

USE mingli_studio;

-- ============================================================
-- 1. 用户关注表 (sys_user_follow)
-- ============================================================
DROP TABLE IF EXISTS sys_user_follow;
CREATE TABLE sys_user_follow (
    id            BIGINT   NOT NULL AUTO_INCREMENT COMMENT '关注ID',
    user_id       BIGINT   NOT NULL COMMENT '用户ID',
    follow_user_id BIGINT  NOT NULL COMMENT '被关注用户ID',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_follow (user_id, follow_user_id),
    KEY idx_user_id (user_id),
    KEY idx_follow_user_id (follow_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注表';

-- ============================================================
-- 2. 消息通知表 (sys_notification)
-- ============================================================
DROP TABLE IF EXISTS sys_notification;
CREATE TABLE sys_notification (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    user_id       BIGINT       NOT NULL COMMENT '接收用户ID',
    sender_id     BIGINT       DEFAULT NULL COMMENT '发送用户ID',
    type          VARCHAR(20)  NOT NULL COMMENT '通知类型(comment/like/favorite/follow/system)',
    title         VARCHAR(200) NOT NULL COMMENT '通知标题',
    content       VARCHAR(500) DEFAULT NULL COMMENT '通知内容',
    target_id     BIGINT       DEFAULT NULL COMMENT '关联目标ID',
    target_type   VARCHAR(20)  DEFAULT NULL COMMENT '目标类型(article/comment)',
    is_read       CHAR(1)      DEFAULT '0' COMMENT '是否已读(0未读 1已读)',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_is_read (is_read),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- ============================================================
-- 3. 用户动态表 (sys_user_activity)
-- ============================================================
DROP TABLE IF EXISTS sys_user_activity;
CREATE TABLE sys_user_activity (
    id            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '动态ID',
    user_id       BIGINT       NOT NULL COMMENT '用户ID',
    type          VARCHAR(20)  NOT NULL COMMENT '动态类型(article/comment/favorite/follow)',
    content       VARCHAR(500) DEFAULT NULL COMMENT '动态内容',
    target_id     BIGINT       DEFAULT NULL COMMENT '关联目标ID',
    target_type   VARCHAR(20)  DEFAULT NULL COMMENT '目标类型(article/comment/user)',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户动态表';

-- ============================================================
-- 4. 用户统计表 (sys_user_stats)
-- ============================================================
DROP TABLE IF EXISTS sys_user_stats;
CREATE TABLE sys_user_stats (
    id              BIGINT NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    article_count   INT    DEFAULT 0 COMMENT '文章数',
    comment_count   INT    DEFAULT 0 COMMENT '评论数',
    favorite_count  INT    DEFAULT 0 COMMENT '收藏数',
    follower_count  INT    DEFAULT 0 COMMENT '粉丝数',
    following_count INT    DEFAULT 0 COMMENT '关注数',
    like_received   INT    DEFAULT 0 COMMENT '获赞数',
    view_count      INT    DEFAULT 0 COMMENT '被浏览数',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户统计表';

-- ============================================================
-- 初始数据
-- ============================================================

-- 为现有用户创建统计记录
INSERT INTO sys_user_stats (user_id) SELECT id FROM sys_user;