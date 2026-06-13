package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.CommunityArticle;
import com.mingli.fortune.mapper.CommunityArticleMapper;
import com.mingli.fortune.service.ICommunityArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 命理文章表 Service实现
 */
@Service
public class CommunityArticleServiceImpl implements ICommunityArticleService {
    
    @Autowired
    private CommunityArticleMapper articleMapper;
    
    @Override
    public List<CommunityArticle> selectArticleList(CommunityArticle article) {
        return articleMapper.selectArticleList(article);
    }
    
    @Override
    public CommunityArticle selectArticleById(Long id) {
        return articleMapper.selectArticleById(id);
    }
    
    @Override
    public int insertArticle(CommunityArticle article) {
        return articleMapper.insertArticle(article);
    }
    
    @Override
    public int updateArticle(CommunityArticle article) {
        return articleMapper.updateArticle(article);
    }
    
    @Override
    public int deleteArticleById(Long id) {
        return articleMapper.deleteArticleById(id);
    }
    
    @Override
    public int deleteArticleByIds(Long[] ids) {
        return articleMapper.deleteArticleByIds(ids);
    }
    
    @Override
    public int updateViewCount(Long id) {
        return articleMapper.updateViewCount(id);
    }
    
    @Override
    public int updateLikeCount(Long id, int increment) {
        return articleMapper.updateLikeCount(id, increment);
    }
    
    @Override
    public int updateCommentCount(Long id, int increment) {
        return articleMapper.updateCommentCount(id, increment);
    }
    
    @Override
    public int updateFavoriteCount(Long id, int increment) {
        return articleMapper.updateFavoriteCount(id, increment);
    }
    
    @Override
    public int countByCategoryId(Long categoryId) {
        return articleMapper.countByCategoryId(categoryId);
    }
    
    @Override
    public int countByAuthorId(Long authorId) {
        return articleMapper.countByAuthorId(authorId);
    }
}