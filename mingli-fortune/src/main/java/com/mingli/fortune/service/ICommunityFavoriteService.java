package com.mingli.fortune.service;

import com.mingli.fortune.domain.CommunityFavorite;

import java.util.List;

/**
 * 用户收藏表 Service接口
 */
public interface ICommunityFavoriteService {
    
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
    CommunityFavorite selectFavorite(Long userId, Long articleId);
    
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
    int deleteFavorite(Long userId, Long articleId);
    
    /**
     * 查询用户收藏数量
     * @param userId 用户ID
     * @return 收藏数量
     */
    int countByUserId(Long userId);
}