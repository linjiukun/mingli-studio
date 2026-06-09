package com.mingli.framework.security;

import com.mingli.common.constant.Constants;
import com.mingli.common.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(Constants.TOKEN_HEADER);

        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录或token已过期\"}");
            return false;
        }

        // 去除 Bearer 前缀
        if (token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.substring(Constants.TOKEN_PREFIX.length());
        }

        if (!JwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token无效或已过期\"}");
            return false;
        }

        Long userId = JwtUtil.getUserId(token);
        request.setAttribute(Constants.LOGIN_USER_KEY, userId);
        return true;
    }
}
