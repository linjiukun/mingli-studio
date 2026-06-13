package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户动态表 sys_user_activity 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserActivity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /** 用户ID */
    private Long userId;
    /** 动态类型(article/comment/favorite/follow) */
    private String type;
    /** 动态内容 */
    private String content;
    /** 关联目标ID */
    private Long targetId;
    /** 目标类型(article/comment/user) */
    private String targetType;

    // 关联字段（非数据库字段）
    /** 用户昵称 */
    private String userName;
    /** 用户头像 */
    private String userAvatar;
}
