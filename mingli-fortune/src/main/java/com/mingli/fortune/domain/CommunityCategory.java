package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章分类表 community_category 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommunityCategory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    private String status;
}