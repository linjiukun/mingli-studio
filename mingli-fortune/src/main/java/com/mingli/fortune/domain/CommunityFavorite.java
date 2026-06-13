package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户收藏表 community_favorite 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityFavorite extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long articleId;
    
    // 关联字段（非数据库字段）
    private String articleTitle;
    private String articleSummary;
    private String articleCoverImage;
}