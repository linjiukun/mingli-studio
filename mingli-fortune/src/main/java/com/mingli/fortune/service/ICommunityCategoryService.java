package com.mingli.fortune.service;

import com.mingli.fortune.domain.CommunityCategory;

import java.util.List;

/**
 * 文章分类表 Service接口
 */
public interface ICommunityCategoryService {
    
    /**
     * 查询分类列表
     * @param category 分类查询条件
     * @return 分类列表
     */
    List<CommunityCategory> selectCategoryList(CommunityCategory category);
    
    /**
     * 查询所有分类
     * @return 分类列表
     */
    List<CommunityCategory> selectAllCategories();
    
    /**
     * 查询分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    CommunityCategory selectCategoryById(Long id);
    
    /**
     * 新增分类
     * @param category 分类信息
     * @return 结果
     */
    int insertCategory(CommunityCategory category);
    
    /**
     * 修改分类
     * @param category 分类信息
     * @return 结果
     */
    int updateCategory(CommunityCategory category);
    
    /**
     * 删除分类
     * @param id 分类ID
     * @return 结果
     */
    int deleteCategoryById(Long id);
}