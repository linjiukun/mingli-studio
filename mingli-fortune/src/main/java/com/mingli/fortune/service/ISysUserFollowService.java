package com.mingli.fortune.service;

import com.mingli.fortune.domain.SysUserFollow;

import java.util.List;

/**
 * 用户关注表 Service接口
 */
public interface ISysUserFollowService {

    /**
     * 查询关注记录
     */
    SysUserFollow selectFollow(Long userId, Long followUserId);

    /**
     * 查询用户的关注列表
     */
    List<SysUserFollow> selectFollowingList(Long userId);

    /**
     * 查询用户的粉丝列表
     */
    List<SysUserFollow> selectFollowerList(Long followUserId);

    /**
     * 关注用户
     */
    int followUser(Long userId, Long followUserId);

    /**
     * 取消关注
     */
    int unfollowUser(Long userId, Long followUserId);

    /**
     * 统计用户关注数
     */
    int countFollowing(Long userId);

    /**
     * 统计用户粉丝数
     */
    int countFollower(Long followUserId);
}
