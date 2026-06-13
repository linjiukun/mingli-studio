package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.SysUserActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户动态表 Mapper接口
 */
@Mapper
public interface SysUserActivityMapper {

    /**
     * 查询动态列表
     */
    List<SysUserActivity> selectActivityList(SysUserActivity activity);

    /**
     * 根据ID查询动态
     */
    SysUserActivity selectActivityById(Long id);

    /**
     * 新增动态
     */
    int insertActivity(SysUserActivity activity);

    /**
     * 删除动态
     */
    int deleteActivityById(Long id);
}
