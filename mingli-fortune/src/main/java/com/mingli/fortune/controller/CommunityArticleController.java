package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.domain.CommunityArticle;
import com.mingli.fortune.domain.CommunityComment;
import com.mingli.fortune.domain.CommunityFavorite;
import com.mingli.fortune.service.ICommunityArticleService;
import com.mingli.fortune.service.ICommunityCommentService;
import com.mingli.fortune.service.ICommunityFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 命理文章 Controller
 */
@RestController
@RequestMapping("/api/community")
public class CommunityArticleController {
    
    @Autowired
    private ICommunityArticleService articleService;
    
    @Autowired
    private ICommunityFavoriteService favoriteService;
    
    @Autowired
    private ICommunityCommentService commentService;
    
    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }
    
    // ==================== 文章相关 ====================
    
    /**
     * 获取文章列表
     */
    @GetMapping("/articles")
    public Result getArticles(@RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        CommunityArticle article = new CommunityArticle();
        article.setCategoryId(categoryId);
        article.setTitle(keyword);
        article.setPageNum(pageNum);
        article.setPageSize(pageSize);
        
        List<CommunityArticle> articles = articleService.selectArticleList(article);
        return Result.success(articles);
    }
    
    /**
     * 获取文章详情
     */
    @GetMapping("/articles/{id}")
    public Result getArticle(@PathVariable Long id, HttpServletRequest request) {
        CommunityArticle article = articleService.selectArticleById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        
        // 更新浏览次数
        articleService.updateViewCount(id);
        
        // 检查是否已收藏
        Long userId = getUserId(request);
        if (userId != null) {
            CommunityFavorite favorite = favoriteService.selectFavorite(userId, id);
            article.setFavoriteCount(favorite != null ? 1 : 0);
        }
        
        return Result.success(article);
    }
    
    /**
     * 发布文章
     */
    @PostMapping("/articles")
    public Result createArticle(@RequestBody CommunityArticle article, HttpServletRequest request) {
        Long userId = getUserId(request);
        article.setAuthorId(userId);
        article.setStatus("1"); // 已发布
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        article.setFavoriteCount(0);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        
        int result = articleService.insertArticle(article);
        if (result > 0) {
            return Result.success("发布成功", article);
        }
        return Result.error("发布失败");
    }
    
    /**
     * 修改文章
     */
    @PutMapping("/articles/{id}")
    public Result updateArticle(@PathVariable Long id, @RequestBody CommunityArticle article, 
                               HttpServletRequest request) {
        Long userId = getUserId(request);
        CommunityArticle existing = articleService.selectArticleById(id);
        
        if (existing == null) {
            return Result.error("文章不存在");
        }
        if (!existing.getAuthorId().equals(userId)) {
            return Result.error("只能修改自己的文章");
        }
        
        article.setId(id);
        article.setUpdateTime(new Date());
        int result = articleService.updateArticle(article);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    
    /**
     * 删除文章
     */
    @DeleteMapping("/articles/{id}")
    public Result deleteArticle(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        CommunityArticle existing = articleService.selectArticleById(id);
        
        if (existing == null) {
            return Result.error("文章不存在");
        }
        if (!existing.getAuthorId().equals(userId)) {
            return Result.error("只能删除自己的文章");
        }
        
        int result = articleService.deleteArticleById(id);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    // ==================== 收藏相关 ====================
    
    /**
     * 收藏文章
     */
    @PostMapping("/articles/{id}/favorite")
    public Result favoriteArticle(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        
        // 检查是否已收藏
        CommunityFavorite existing = favoriteService.selectFavorite(userId, id);
        if (existing != null) {
            return Result.error("已收藏该文章");
        }
        
        CommunityFavorite favorite = new CommunityFavorite();
        favorite.setUserId(userId);
        favorite.setArticleId(id);
        favorite.setCreateTime(new Date());
        
        int result = favoriteService.insertFavorite(favorite);
        if (result > 0) {
            // 更新文章收藏数
            articleService.updateFavoriteCount(id, 1);
            return Result.success("收藏成功");
        }
        return Result.error("收藏失败");
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/articles/{id}/favorite")
    public Result unfavoriteArticle(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        
        int result = favoriteService.deleteFavorite(userId, id);
        if (result > 0) {
            // 更新文章收藏数
            articleService.updateFavoriteCount(id, -1);
            return Result.success("取消收藏成功");
        }
        return Result.error("取消收藏失败");
    }
    
    /**
     * 获取用户收藏列表
     */
    @GetMapping("/favorites")
    public Result getFavorites(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<CommunityFavorite> favorites = favoriteService.selectFavoritesByUserId(userId);
        return Result.success(favorites);
    }
    
    // ==================== 评论相关 ====================
    
    /**
     * 获取文章评论
     */
    @GetMapping("/articles/{id}/comments")
    public Result getComments(@PathVariable Long id) {
        List<CommunityComment> comments = commentService.selectCommentsByArticleId(id);
        return Result.success(comments);
    }
    
    /**
     * 发表评论
     */
    @PostMapping("/articles/{id}/comments")
    public Result createComment(@PathVariable Long id, @RequestBody Map<String, String> params,
                               HttpServletRequest request) {
        Long userId = getUserId(request);
        String content = params.get("content");
        String parentIdStr = params.get("parentId");
        
        if (content == null || content.trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }
        
        CommunityComment comment = new CommunityComment();
        comment.setUserId(userId);
        comment.setArticleId(id);
        comment.setContent(content);
        comment.setParentId(parentIdStr != null ? Long.parseLong(parentIdStr) : null);
        comment.setLikeCount(0);
        comment.setStatus("0");
        comment.setCreateTime(new Date());
        
        int result = commentService.insertComment(comment);
        if (result > 0) {
            // 更新文章评论数
            articleService.updateCommentCount(id, 1);
            return Result.success("评论成功", comment);
        }
        return Result.error("评论失败");
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/comments/{id}")
    public Result deleteComment(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        CommunityComment existing = commentService.selectCommentById(id);
        
        if (existing == null) {
            return Result.error("评论不存在");
        }
        if (!existing.getUserId().equals(userId)) {
            return Result.error("只能删除自己的评论");
        }
        
        int result = commentService.deleteCommentById(id);
        if (result > 0) {
            // 更新文章评论数
            articleService.updateCommentCount(existing.getArticleId(), -1);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/comments/{id}/like")
    public Result likeComment(@PathVariable Long id) {
        int result = commentService.updateLikeCount(id, 1);
        if (result > 0) {
            return Result.success("点赞成功");
        }
        return Result.error("点赞失败");
    }
}