package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.CommunityComment;
import com.mingli.fortune.mapper.CommunityCommentMapper;
import com.mingli.fortune.service.ICommunityCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章评论表 Service实现
 */
@Service
public class CommunityCommentServiceImpl implements ICommunityCommentService {
    
    @Autowired
    private CommunityCommentMapper commentMapper;
    
    @Override
    public List<CommunityComment> selectCommentsByArticleId(Long articleId) {
        return commentMapper.selectCommentsByArticleId(articleId);
    }
    
    @Override
    public CommunityComment selectCommentById(Long id) {
        return commentMapper.selectCommentById(id);
    }
    
    @Override
    public int insertComment(CommunityComment comment) {
        return commentMapper.insertComment(comment);
    }
    
    @Override
    public int updateComment(CommunityComment comment) {
        return commentMapper.updateComment(comment);
    }
    
    @Override
    public int deleteCommentById(Long id) {
        return commentMapper.deleteCommentById(id);
    }
    
    @Override
    public int deleteCommentByIds(Long[] ids) {
        return commentMapper.deleteCommentByIds(ids);
    }
    
    @Override
    public List<CommunityComment> selectRepliesByParentId(Long parentId) {
        return commentMapper.selectRepliesByParentId(parentId);
    }
    
    @Override
    public int countByUserId(Long userId) {
        return commentMapper.countByUserId(userId);
    }
    
    @Override
    public int updateLikeCount(Long id, int increment) {
        return commentMapper.updateLikeCount(id, increment);
    }
}