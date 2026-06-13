package com.mingli.fortune.service;

import com.mingli.fortune.domain.SysNotification;

import java.util.List;

/**
 * 消息通知表 Service接口
 */
public interface ISysNotificationService {

    /**
     * 查询通知列表
     */
    List<SysNotification> selectNotificationList(SysNotification notification);

    /**
     * 根据ID查询通知
     */
    SysNotification selectNotificationById(Long id);

    /**
     * 发送通知
     */
    int sendNotification(SysNotification notification);

    /**
     * 标记单条通知为已读
     */
    int markAsRead(Long id);

    /**
     * 标记用户所有通知为已读
     */
    int markAllAsRead(Long userId);

    /**
     * 统计用户未读通知数量
     */
    int countUnread(Long userId);

    /**
     * 删除通知
     */
    int deleteNotificationById(Long id);

    /**
     * 批量删除通知
     */
    int deleteNotificationByIds(Long[] ids);
}
