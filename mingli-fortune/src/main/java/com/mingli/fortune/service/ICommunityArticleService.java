package com.mingli.fortune.service;

import com.mingli.fortune.domain.CommunityArticle;

import java.util.List;

/**
 * 命理文章表 Service接口
 */
public interface ICommunityArticleService {
    
    /**
     * 查询文章列表
     * @param article 文章查询条件
     * @return 文章列表
     */
    List<CommunityArticle> selectArticleList(CommunityArticle article);
    
    /**
     * 查询文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    CommunityArticle selectArticleById(Long id);
    
    /**
     * 新增文章
     * @param article 文章信息
     * @return 结果
     */
    int insertArticle(CommunityArticle article);
    
    /**
     * 修改文章
     * @param article 文章信息
     * @return 结果
     */
    int updateArticle(CommunityArticle article);
    
    /**
     * 删除文章
     * @param id 文章ID
     * @return 结果
     */
    int deleteArticleById(Long id);
    
    /**
     * 批量删除文章
     * @param ids 需要删除的文章ID
     * @return 结果
     */
    int deleteArticleByIds(Long[] ids);
    
    /**
     * 更新文章浏览次数
     * @param id 文章ID
     * @return 结果
     */
    int updateViewCount(Long id);
    
    /**
     * 更新文章点赞次数
     * @param id 文章ID
     * @param increment 增量
     * @return 结果
     */
    int updateLikeCount(Long id, int increment);
    
    /**
     * 更新文章评论次数
     * @param id 文章ID
     * @param increment 增量
     * @return 结果
     */
    int updateCommentCount(Long id, int increment);
    
    /**
     * 更新文章收藏次数
     * @param id 文章ID
     * @param increment 增量
     * @return 结果
     */
    int updateFavoriteCount(Long id, int increment);
    
    /**
     * 根据分类ID查询文章数量
     * @param categoryId 分类ID
     * @return 文章数量
     */
    int countByCategoryId(Long categoryId);
    
    /**
     * 根据作者ID查询文章数量
     * @param authorId 作者ID
     * @return 文章数量
     */
    int countByAuthorId(Long authorId);
}