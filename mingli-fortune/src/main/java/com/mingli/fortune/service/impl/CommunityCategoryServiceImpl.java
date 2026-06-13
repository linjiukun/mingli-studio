package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.CommunityCategory;
import com.mingli.fortune.mapper.CommunityCategoryMapper;
import com.mingli.fortune.service.ICommunityCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章分类表 Service实现
 */
@Service
public class CommunityCategoryServiceImpl implements ICommunityCategoryService {
    
    @Autowired
    private CommunityCategoryMapper categoryMapper;
    
    @Override
    public List<CommunityCategory> selectCategoryList(CommunityCategory category) {
        return categoryMapper.selectCategoryList(category);
    }
    
    @Override
    public List<CommunityCategory> selectAllCategories() {
        return categoryMapper.selectAllCategories();
    }
    
    @Override
    public CommunityCategory selectCategoryById(Long id) {
        return categoryMapper.selectCategoryById(id);
    }
    
    @Override
    public int insertCategory(CommunityCategory category) {
        return categoryMapper.insertCategory(category);
    }
    
    @Override
    public int updateCategory(CommunityCategory category) {
        return categoryMapper.updateCategory(category);
    }
    
    @Override
    public int deleteCategoryById(Long id) {
        return categoryMapper.deleteCategoryById(id);
    }
}