package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 占卜记录表 fortune_divination 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FortuneDivination extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private String question;
    private String divinationMethod;
    private String hexagramName;
    private String hexagramSymbol;
    private String interpretation;
    private String advice;
}
