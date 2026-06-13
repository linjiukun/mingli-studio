package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.CommunityFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户收藏表 Mapper接口
 */
@Mapper
public interface CommunityFavoriteMapper {
    
    /**
     * 查询用户收藏列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<CommunityFavorite> selectFavoritesByUserId(Long userId);
    
    /**
     * 查询用户是否收藏文章
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 收藏信息
     */
    CommunityFavorite selectFavorite(@Param("userId") Long userId, @Param("articleId") Long articleId);
    
    /**
     * 新增收藏
     * @param favorite 收藏信息
     * @return 结果
     */
    int insertFavorite(CommunityFavorite favorite);
    
    /**
     * 删除收藏
     * @param userId 用户ID
     * @param articleId 文章ID
     * @return 结果
     */
    int deleteFavorite(@Param("userId") Long userId, @Param("articleId") Long articleId);
    
    /**
     * 查询用户收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    int countByUserId(Long userId);
}