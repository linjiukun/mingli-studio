package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户关注表 sys_user_follow 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserFollow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /** 用户ID */
    private Long userId;
    /** 被关注用户ID */
    private Long followUserId;

    // 关联字段（非数据库字段）
    /** 被关注用户昵称 */
    private String followUserName;
    /** 被关注用户头像 */
    private String followUserAvatar;
}
