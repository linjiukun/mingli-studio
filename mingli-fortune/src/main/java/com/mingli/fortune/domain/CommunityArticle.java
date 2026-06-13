package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 命理文章表 community_article 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityArticle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Long categoryId;
    private Long authorId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private String status;
    
    // 关联字段（非数据库字段）
    private String categoryName;
    private String authorName;
    private String authorAvatar;
}