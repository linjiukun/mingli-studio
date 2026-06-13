package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.SysUserStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户统计表 Mapper接口
 */
@Mapper
public interface SysUserStatsMapper {

    /**
     * 根据用户ID查询统计
     */
    SysUserStats selectStatsByUserId(Long userId);

    /**
     * 新增统计记录
     */
    int insertStats(SysUserStats stats);

    /**
     * 更新统计记录
     */
    int updateStats(SysUserStats stats);

    /**
     * 增量更新关注数
     */
    int updateFollowingCount(@Param("userId") Long userId, @Param("increment") int increment);

    /**
     * 增量更新粉丝数
     */
    int updateFollowerCount(@Param("userId") Long userId, @Param("increment") int increment);
}
