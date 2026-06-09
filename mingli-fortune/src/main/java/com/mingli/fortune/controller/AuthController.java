package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.system.domain.SysUser;
import com.mingli.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ISysUserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody SysUser user) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().length() < 6) {
                return Result.error("密码至少6位");
            }
            SysUser registered = userService.register(user);
            registered.setPassword(null);
            return Result.success("注册成功", registered);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String password = params.get("password");
            if (username == null || password == null) {
                return Result.error("请输入用户名和密码");
            }
            Map<String, Object> result = userService.login(username, password);
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
