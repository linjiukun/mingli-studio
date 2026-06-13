package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.SysUserFollow;
import com.mingli.fortune.domain.SysUserStats;
import com.mingli.fortune.mapper.SysUserFollowMapper;
import com.mingli.fortune.mapper.SysUserStatsMapper;
import com.mingli.fortune.service.ISysUserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户关注表 Service实现
 */
@Service
public class SysUserFollowServiceImpl implements ISysUserFollowService {

    @Autowired
    private SysUserFollowMapper followMapper;

    @Autowired
    private SysUserStatsMapper statsMapper;

    @Override
    public SysUserFollow selectFollow(Long userId, Long followUserId) {
        return followMapper.selectFollow(userId, followUserId);
    }

    @Override
    public List<SysUserFollow> selectFollowingList(Long userId) {
        return followMapper.selectFollowingList(userId);
    }

    @Override
    public List<SysUserFollow> selectFollowerList(Long followUserId) {
        return followMapper.selectFollowerList(followUserId);
    }

    @Override
    @Transactional
    public int followUser(Long userId, Long followUserId) {
        // 不能关注自己
        if (userId.equals(followUserId)) {
            return 0;
        }
        // 检查是否已关注
        SysUserFollow existing = followMapper.selectFollow(userId, followUserId);
        if (existing != null) {
            return 0;
        }

        SysUserFollow follow = new SysUserFollow();
        follow.setUserId(userId);
        follow.setFollowUserId(followUserId);
        follow.setCreateTime(new Date());

        int result = followMapper.insertFollow(follow);
        if (result > 0) {
            // 更新统计：关注者的关注数+1，被关注者的粉丝数+1
            statsMapper.updateFollowingCount(userId, 1);
            statsMapper.updateFollowerCount(followUserId, 1);
        }
        return result;
    }

    @Override
    @Transactional
    public int unfollowUser(Long userId, Long followUserId) {
        int result = followMapper.deleteFollow(userId, followUserId);
        if (result > 0) {
            // 更新统计：关注者的关注数-1，被关注者的粉丝数-1
            statsMapper.updateFollowingCount(userId, -1);
            statsMapper.updateFollowerCount(followUserId, -1);
        }
        return result;
    }

    @Override
    public int countFollowing(Long userId) {
        return followMapper.countFollowing(userId);
    }

    @Override
    public int countFollower(Long followUserId) {
        return followMapper.countFollower(followUserId);
    }
}
