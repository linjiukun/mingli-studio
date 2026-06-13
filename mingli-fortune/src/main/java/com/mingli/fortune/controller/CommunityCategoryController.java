package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.domain.CommunityCategory;
import com.mingli.fortune.service.ICommunityCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 文章分类 Controller
 */
@RestController
@RequestMapping("/api/community/categories")
public class CommunityCategoryController {
    
    @Autowired
    private ICommunityCategoryService categoryService;
    
    /**
     * 获取所有分类
     */
    @GetMapping
    public Result getCategories() {
        List<CommunityCategory> categories = categoryService.selectAllCategories();
        return Result.success(categories);
    }
    
    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    public Result getCategory(@PathVariable Long id) {
        CommunityCategory category = categoryService.selectCategoryById(id);
        if (category == null) {
            return Result.error("分类不存在");
        }
        return Result.success(category);
    }
    
    /**
     * 新增分类（管理员）
     */
    @PostMapping
    public Result createCategory(@RequestBody CommunityCategory category) {
        category.setCreateTime(new Date());
        category.setStatus("0");
        
        int result = categoryService.insertCategory(category);
        if (result > 0) {
            return Result.success("创建成功", category);
        }
        return Result.error("创建失败");
    }
    
    /**
     * 修改分类（管理员）
     */
    @PutMapping("/{id}")
    public Result updateCategory(@PathVariable Long id, @RequestBody CommunityCategory category) {
        category.setId(id);
        int result = categoryService.updateCategory(category);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    
    /**
     * 删除分类（管理员）
     */
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable Long id) {
        int result = categoryService.deleteCategoryById(id);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}