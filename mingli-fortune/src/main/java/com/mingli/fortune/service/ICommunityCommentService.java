package com.mingli.fortune.service;

import com.mingli.fortune.domain.CommunityComment;

import java.util.List;

/**
 * 文章评论表 Service接口
 */
public interface ICommunityCommentService {
    
    /**
     * 查询文章评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<CommunityComment> selectCommentsByArticleId(Long articleId);
    
    /**
     * 查询评论详情
     * @param id 评论ID
     * @return 评论详情
     */
    CommunityComment selectCommentById(Long id);
    
    /**
     * 新增评论
     * @param comment 评论信息
     * @return 结果
     */
    int insertComment(CommunityComment comment);
    
    /**
     * 修改评论
     * @param comment 评论信息
     * @return 结果
     */
    int updateComment(CommunityComment comment);
    
    /**
     * 删除评论
     * @param id 评论ID
     * @return 结果
     */
    int deleteCommentById(Long id);
    
    /**
     * 批量删除评论
     * @param ids 需要删除的评论ID
     * @return 结果
     */
    int deleteCommentByIds(Long[] ids);
    
    /**
     * 查询评论的子评论
     * @param parentId 父评论ID
     * @return 子评论列表
     */
    List<CommunityComment> selectRepliesByParentId(Long parentId);
    
    /**
     * 查询用户评论数量
     * @param userId 用户ID
     * @return 评论数量
     */
    int countByUserId(Long userId);
    
    /**
     * 更新评论点赞次数
     * @param id 评论ID
     * @param increment 增量
     * @return 结果
     */
    int updateLikeCount(Long id, int increment);
}