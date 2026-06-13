package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.CommunityCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章分类表 Mapper接口
 */
@Mapper
public interface CommunityCategoryMapper {

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

    /**
     * 检查分类名称是否唯一
     * @param name 分类名称
     * @param id 分类ID（排除自身）
     * @return 结果
     */
    int checkCategoryNameUnique(@Param("name") String name, @Param("id") Long id);
}
