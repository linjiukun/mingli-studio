package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 命理师表 fortune_consultant 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FortuneConsultant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String realName;
    private String introduction;
    private String expertise;
    private String certification;
    private Double rating;
    private Integer consultationCount;
    private String status;
    
    // 关联字段（非数据库字段）
    private String userName;
    private String userAvatar;
}