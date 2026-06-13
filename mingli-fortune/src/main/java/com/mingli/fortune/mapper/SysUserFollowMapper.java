package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.SysUserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关注表 Mapper接口
 */
@Mapper
public interface SysUserFollowMapper {

    /**
     * 查询关注记录
     */
    SysUserFollow selectFollow(@Param("userId") Long userId, @Param("followUserId") Long followUserId);

    /**
     * 查询用户的关注列表
     */
    List<SysUserFollow> selectFollowingList(Long userId);

    /**
     * 查询用户的粉丝列表
     */
    List<SysUserFollow> selectFollowerList(Long followUserId);

    /**
     * 新增关注
     */
    int insertFollow(SysUserFollow follow);

    /**
     * 取消关注
     */
    int deleteFollow(@Param("userId") Long userId, @Param("followUserId") Long followUserId);

    /**
     * 统计用户关注数
     */
    int countFollowing(Long userId);

    /**
     * 统计用户粉丝数
     */
    int countFollower(Long followUserId);
}
