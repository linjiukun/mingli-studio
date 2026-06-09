package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 命理解读记录表 fortune_reading 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FortuneReading extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long chartId;
    private String readingType;
    private String content;
    private Integer score;
}
