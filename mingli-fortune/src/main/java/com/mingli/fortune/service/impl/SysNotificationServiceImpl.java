package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.SysNotification;
import com.mingli.fortune.mapper.SysNotificationMapper;
import com.mingli.fortune.service.ISysNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息通知表 Service实现
 */
@Service
public class SysNotificationServiceImpl implements ISysNotificationService {

    @Autowired
    private SysNotificationMapper notificationMapper;

    @Override
    public List<SysNotification> selectNotificationList(SysNotification notification) {
        return notificationMapper.selectNotificationList(notification);
    }

    @Override
    public SysNotification selectNotificationById(Long id) {
        return notificationMapper.selectNotificationById(id);
    }

    @Override
    public int sendNotification(SysNotification notification) {
        return notificationMapper.insertNotification(notification);
    }

    @Override
    public int markAsRead(Long id) {
        return notificationMapper.updateReadStatus(id);
    }

    @Override
    public int markAllAsRead(Long userId) {
        return notificationMapper.updateAllReadStatus(userId);
    }

    @Override
    public int countUnread(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    public int deleteNotificationById(Long id) {
        return notificationMapper.deleteNotificationById(id);
    }

    @Override
    public int deleteNotificationByIds(Long[] ids) {
        return notificationMapper.deleteNotificationByIds(ids);
    }
}
