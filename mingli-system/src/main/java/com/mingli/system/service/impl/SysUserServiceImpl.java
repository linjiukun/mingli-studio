package com.mingli.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mingli.common.constant.Constants;
import com.mingli.common.utils.JwtUtil;
import com.mingli.system.domain.SysUser;
import com.mingli.system.mapper.SysUserMapper;
import com.mingli.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public SysUser register(SysUser user) {
        // 检查用户名是否已存在
        SysUser existing = sysUserMapper.selectByUsername(user.getUsername());
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("0");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        sysUserMapper.insert(user);
        return user;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if ("1".equals(user.getStatus())) {
            throw new RuntimeException("账号已被停用");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 生成 JWT Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        return result;
    }

    @Override
    public SysUser selectById(Long id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public SysUser selectByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public int updateProfile(SysUser user) {
        user.setUpdateTime(new Date());
        // 密码单独处理，不走此处更新
        user.setPassword(null);
        return sysUserMapper.update(user);
    }
}
