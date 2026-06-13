package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.SysNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息通知表 Mapper接口
 */
@Mapper
public interface SysNotificationMapper {

    /**
     * 查询通知列表
     */
    List<SysNotification> selectNotificationList(SysNotification notification);

    /**
     * 根据ID查询通知
     */
    SysNotification selectNotificationById(Long id);

    /**
     * 新增通知
     */
    int insertNotification(SysNotification notification);

    /**
     * 标记单条通知为已读
     */
    int updateReadStatus(@Param("id") Long id);

    /**
     * 标记用户所有通知为已读
     */
    int updateAllReadStatus(Long userId);

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
