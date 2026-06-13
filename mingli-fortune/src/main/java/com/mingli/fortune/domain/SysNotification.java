package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息通知表 sys_notification 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNotification extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /** 接收用户ID */
    private Long userId;
    /** 发送用户ID */
    private Long senderId;
    /** 通知类型(comment/like/favorite/follow/system) */
    private String type;
    /** 通知标题 */
    private String title;
    /** 通知内容 */
    private String content;
    /** 关联目标ID */
    private Long targetId;
    /** 目标类型(article/comment) */
    private String targetType;
    /** 是否已读(0未读 1已读) */
    private String isRead;

    // 关联字段（非数据库字段）
    /** 发送用户昵称 */
    private String senderName;
    /** 发送用户头像 */
    private String senderAvatar;
}
