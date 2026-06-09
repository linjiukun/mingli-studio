package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 每日运势表 fortune_daily 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FortuneDaily extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Date fortuneDate;
    private Integer overallScore;
    private Integer wealthScore;
    private Integer loveScore;
    private Integer careerScore;
    private Integer healthScore;
    private String luckyColor;
    private String luckyNumber;
    private String luckyDirection;
    private String avoidDirection;
    private String advice;
}
