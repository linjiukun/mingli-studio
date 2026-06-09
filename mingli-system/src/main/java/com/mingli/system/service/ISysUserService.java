package com.mingli.system.service;

import com.mingli.system.domain.SysUser;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface ISysUserService {

    /**
     * 用户注册
     */
    SysUser register(SysUser user);

    /**
     * 用户登录验证
     */
    Map<String, Object> login(String username, String password);

    /**
     * 根据ID查询用户
     */
    SysUser selectById(Long id);

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(String username);

    /**
     * 更新用户信息
     */
    int updateProfile(SysUser user);
}
