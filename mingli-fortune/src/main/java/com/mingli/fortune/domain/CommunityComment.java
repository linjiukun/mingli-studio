package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文章评论表 community_comment 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityComment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long articleId;
    private String content;
    private Long parentId;
    private Integer likeCount;
    private String status;
    
    // 关联字段（非数据库字段）
    private String userName;
    private String userAvatar;
    private List<CommunityComment> replies; // 子评论
}