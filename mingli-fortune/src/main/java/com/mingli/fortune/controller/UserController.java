package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.system.domain.SysUser;
import com.mingli.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private ISysUserService userService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    @GetMapping("/profile")
    public Result profile(HttpServletRequest request) {
        Long userId = getUserId(request);
        SysUser user = userService.selectById(userId);
        if (user == null) return Result.error("用户不存在");
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestBody SysUser user, HttpServletRequest request) {
        Long userId = getUserId(request);
        user.setId(userId);
        int rows = userService.updateProfile(user);
        return rows > 0 ? Result.success("更新成功") : Result.error("更新失败");
    }
}
