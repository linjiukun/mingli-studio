package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 八字命盘表 fortune_birth_chart 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FortuneBirthChart extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Date birthDate;
    private String birthHour;
    private String gender;
    private String yearPillar;
    private String monthPillar;
    private String dayPillar;
    private String hourPillar;
    private String heavenlyStems;
    private String earthlyBranches;
    private String fiveElements;
    private String luckGods;
    private String unluckGods;
    private String summary;
}
