package com.mingli.fortune.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户统计表 sys_user_stats 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserStats extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    /** 用户ID */
    private Long userId;
    /** 文章数 */
    private Integer articleCount;
    /** 评论数 */
    private Integer commentCount;
    /** 收藏数 */
    private Integer favoriteCount;
    /** 粉丝数 */
    private Integer followerCount;
    /** 关注数 */
    private Integer followingCount;
    /** 获赞数 */
    private Integer likeReceived;
    /** 被浏览数 */
    private Integer viewCount;
    /** 更新时间 */
    private Date updateTime;
}
