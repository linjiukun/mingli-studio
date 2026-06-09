package com.mingli.system.mapper;

import com.mingli.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 Mapper 接口
 */
public interface SysUserMapper {

    /**
     * 根据ID查询用户
     */
    SysUser selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 查询用户列表（支持分页）
     */
    List<SysUser> selectList(SysUser user);

    /**
     * 新增用户
     */
    int insert(SysUser user);

    /**
     * 更新用户
     */
    int update(SysUser user);

    /**
     * 删除用户
     */
    int deleteById(@Param("id") Long id);
}
