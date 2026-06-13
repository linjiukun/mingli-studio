package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.domain.SysNotification;
import com.mingli.fortune.service.ISysNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 消息通知 Controller
 */
@RestController
@RequestMapping("/api/notification")
public class SysNotificationController {

    @Autowired
    private ISysNotificationService notificationService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    /**
     * 获取通知列表
     */
    @GetMapping("/list")
    public Result getNotificationList(@RequestParam(required = false) String type,
                                      @RequestParam(required = false) String isRead,
                                      HttpServletRequest request) {
        Long userId = getUserId(request);
        SysNotification query = new SysNotification();
        query.setUserId(userId);
        query.setType(type);
        query.setIsRead(isRead);
        List<SysNotification> list = notificationService.selectNotificationList(query);
        return Result.success(list);
    }

    /**
     * 获取通知详情
     */
    @GetMapping("/{id}")
    public Result getNotification(@PathVariable Long id) {
        SysNotification notification = notificationService.selectNotificationById(id);
        if (notification == null) {
            return Result.error("通知不存在");
        }
        return Result.success(notification);
    }

    /**
     * 标记单条通知为已读
     */
    @PutMapping("/{id}/read")
    public Result markAsRead(@PathVariable Long id) {
        int result = notificationService.markAsRead(id);
        if (result > 0) {
            return Result.success("标记成功");
        }
        return Result.error("标记失败");
    }

    /**
     * 标记所有通知为已读
     */
    @PutMapping("/read-all")
    public Result markAllAsRead(HttpServletRequest request) {
        Long userId = getUserId(request);
        int result = notificationService.markAllAsRead(userId);
        return Result.success("已全部标记为已读", result);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread/count")
    public Result getUnreadCount(HttpServletRequest request) {
        Long userId = getUserId(request);
        int count = notificationService.countUnread(userId);
        return Result.success(count);
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    public Result deleteNotification(@PathVariable Long id) {
        int result = notificationService.deleteNotificationById(id);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 批量删除通知
     */
    @DeleteMapping("/batch")
    public Result deleteNotifications(@RequestBody Map<String, Long[]> params) {
        Long[] ids = params.get("ids");
        if (ids == null || ids.length == 0) {
            return Result.error("请选择要删除的通知");
        }
        int result = notificationService.deleteNotificationByIds(ids);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
