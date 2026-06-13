package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.CommunityFavorite;
import com.mingli.fortune.mapper.CommunityFavoriteMapper;
import com.mingli.fortune.service.ICommunityFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收藏表 Service实现
 */
@Service
public class CommunityFavoriteServiceImpl implements ICommunityFavoriteService {
    
    @Autowired
    private CommunityFavoriteMapper favoriteMapper;
    
    @Override
    public List<CommunityFavorite> selectFavoritesByUserId(Long userId) {
        return favoriteMapper.selectFavoritesByUserId(userId);
    }
    
    @Override
    public CommunityFavorite selectFavorite(Long userId, Long articleId) {
        return favoriteMapper.selectFavorite(userId, articleId);
    }
    
    @Override
    public int insertFavorite(CommunityFavorite favorite) {
        return favoriteMapper.insertFavorite(favorite);
    }
    
    @Override
    public int deleteFavorite(Long userId, Long articleId) {
        return favoriteMapper.deleteFavorite(userId, articleId);
    }
    
    @Override
    public int countByUserId(Long userId) {
        return favoriteMapper.countByUserId(userId);
    }
}