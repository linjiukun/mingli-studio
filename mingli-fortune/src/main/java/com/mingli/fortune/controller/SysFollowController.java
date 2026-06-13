package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.domain.SysUserFollow;
import com.mingli.fortune.service.ISysUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户关注 Controller
 */
@RestController
@RequestMapping("/api/social")
public class SysFollowController {

    @Autowired
    private ISysUserFollowService followService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    /**
     * 关注用户
     */
    @PostMapping("/follow/{followUserId}")
    public Result followUser(@PathVariable Long followUserId, HttpServletRequest request) {
        Long userId = getUserId(request);
        int result = followService.followUser(userId, followUserId);
        if (result > 0) {
            return Result.success("关注成功");
        }
        return Result.error("关注失败，可能已关注或不能关注自己");
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/follow/{followUserId}")
    public Result unfollowUser(@PathVariable Long followUserId, HttpServletRequest request) {
        Long userId = getUserId(request);
        int result = followService.unfollowUser(userId, followUserId);
        if (result > 0) {
            return Result.success("取消关注成功");
        }
        return Result.error("取消关注失败");
    }

    /**
     * 查询是否已关注
     */
    @GetMapping("/follow/check/{followUserId}")
    public Result checkFollow(@PathVariable Long followUserId, HttpServletRequest request) {
        Long userId = getUserId(request);
        SysUserFollow follow = followService.selectFollow(userId, followUserId);
        return Result.success(follow != null);
    }

    /**
     * 获取关注列表
     */
    @GetMapping("/following")
    public Result getFollowingList(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<SysUserFollow> list = followService.selectFollowingList(userId);
        return Result.success(list);
    }

    /**
     * 获取指定用户的关注列表
     */
    @GetMapping("/following/{userId}")
    public Result getFollowingListByUserId(@PathVariable Long userId) {
        List<SysUserFollow> list = followService.selectFollowingList(userId);
        return Result.success(list);
    }

    /**
     * 获取粉丝列表
     */
    @GetMapping("/followers")
    public Result getFollowerList(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<SysUserFollow> list = followService.selectFollowerList(userId);
        return Result.success(list);
    }

    /**
     * 获取指定用户的粉丝列表
     */
    @GetMapping("/followers/{userId}")
    public Result getFollowerListByUserId(@PathVariable Long userId) {
        List<SysUserFollow> list = followService.selectFollowerList(userId);
        return Result.success(list);
    }

    /**
     * 获取关注数
     */
    @GetMapping("/following/count")
    public Result getFollowingCount(HttpServletRequest request) {
        Long userId = getUserId(request);
        int count = followService.countFollowing(userId);
        return Result.success(count);
    }

    /**
     * 获取粉丝数
     */
    @GetMapping("/followers/count")
    public Result getFollowerCount(HttpServletRequest request) {
        Long userId = getUserId(request);
        int count = followService.countFollower(userId);
        return Result.success(count);
    }
}
