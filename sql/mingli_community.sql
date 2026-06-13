-- ============================================================
-- 命理阁社区模块数据库脚本
-- 数据库: MariaDB
-- ============================================================

USE mingli_studio;

-- ============================================================
-- 1. 文章分类表 (community_category)
-- ============================================================
DROP TABLE IF EXISTS community_category;
CREATE TABLE community_category (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    name        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    description VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    sort_order  INT          DEFAULT 0 COMMENT '排序',
    status      CHAR(1)      DEFAULT '0' COMMENT '状态(0正常 1停用)',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- ============================================================
-- 2. 命理文章表 (community_article)
-- ============================================================
DROP TABLE IF EXISTS community_article;
CREATE TABLE community_article (
    id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    title           VARCHAR(200) NOT NULL COMMENT '标题',
    content         MEDIUMTEXT   NOT NULL COMMENT '内容',
    summary         VARCHAR(500) DEFAULT NULL COMMENT '摘要',
    cover_image     VARCHAR(500) DEFAULT NULL COMMENT '封面图片URL',
    category_id     BIGINT       NOT NULL COMMENT '分类ID',
    author_id       BIGINT       NOT NULL COMMENT '作者ID',
    view_count      INT          DEFAULT 0 COMMENT '浏览次数',
    like_count      INT          DEFAULT 0 COMMENT '点赞次数',
    comment_count   INT          DEFAULT 0 COMMENT '评论次数',
    favorite_count  INT          DEFAULT 0 COMMENT '收藏次数',
    status          CHAR(1)      DEFAULT '0' COMMENT '状态(0草稿 1已发布 2已下架)',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_category_id (category_id),
    KEY idx_author_id (author_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命理文章表';

-- ============================================================
-- 3. 用户收藏表 (community_favorite)
-- ============================================================
DROP TABLE IF EXISTS community_favorite;
CREATE TABLE community_favorite (
    id          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    user_id     BIGINT   NOT NULL COMMENT '用户ID',
    article_id  BIGINT   NOT NULL COMMENT '文章ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_article (user_id, article_id),
    KEY idx_user_id (user_id),
    KEY idx_article_id (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- ============================================================
-- 4. 文章评论表 (community_comment)
-- ============================================================
DROP TABLE IF EXISTS community_comment;
CREATE TABLE community_comment (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    article_id  BIGINT       NOT NULL COMMENT '文章ID',
    content     VARCHAR(1000) NOT NULL COMMENT '评论内容',
    parent_id   BIGINT       DEFAULT NULL COMMENT '父评论ID',
    like_count  INT          DEFAULT 0 COMMENT '点赞次数',
    status      CHAR(1)      DEFAULT '0' COMMENT '状态(0正常 1已删除)',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_article_id (article_id),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';

-- ============================================================
-- 5. 命理师表 (fortune_consultant)
-- ============================================================
DROP TABLE IF EXISTS fortune_consultant;
CREATE TABLE fortune_consultant (
    id                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '命理师ID',
    user_id           BIGINT       NOT NULL COMMENT '用户ID',
    real_name         VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    introduction      TEXT         DEFAULT NULL COMMENT '简介',
    expertise         VARCHAR(200) DEFAULT NULL COMMENT '擅长领域(八字/风水/星座等)',
    certification     VARCHAR(200) DEFAULT NULL COMMENT '认证信息',
    rating            DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    consultation_count INT         DEFAULT 0 COMMENT '咨询次数',
    status            CHAR(1)      DEFAULT '0' COMMENT '状态(0待审核 1已认证 2已禁用)',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='命理师表';

-- ============================================================
-- 初始数据
-- ============================================================

-- 插入文章分类
INSERT INTO community_category (name, description, sort_order, status) VALUES
('八字命理', '八字算命、四柱预测、命理分析', 1, '0'),
('风水堪舆', '家居风水、办公室风水、风水布局', 2, '0'),
('星座运势', '十二星座分析、星座运势、星座配对', 3, '0'),
('易经占卜', '六爻预测、梅花易数、易经智慧', 4, '0'),
('姓名学', '姓名测试、五格剖象、起名改名', 5, '0'),
('紫微斗数', '紫微命盘、星曜分析、运势预测', 6, '0'),
('相术面相', '面相手相、体相分析、相术入门', 7, '0'),
('命理杂谈', '命理故事、文化探讨、学习心得', 8, '0');

-- 插入示例命理师
INSERT INTO fortune_consultant (user_id, real_name, introduction, expertise, certification, rating, consultation_count, status) VALUES
(1, '系统管理员', '命理阁创始人，精通八字命理、风水堪舆，拥有多年命理咨询经验。', '八字命理,风水堪舆,易经占卜', '国家易学文化传承师', 5.0, 100, '1');