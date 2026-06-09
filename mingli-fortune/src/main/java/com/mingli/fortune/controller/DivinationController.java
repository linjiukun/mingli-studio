package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.service.IDivinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/divination")
public class DivinationController {

    @Autowired
    private IDivinationService divinationService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    @PostMapping("/ask")
    public Result ask(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = getUserId(request);
        String question = params.get("question");
        String method = params.get("method");
        if (question == null || question.trim().isEmpty()) {
            return Result.error("请输入您要占问的问题");
        }
        if (method == null || method.trim().isEmpty()) {
            method = "六爻";
        }
        Map<String, Object> result = divinationService.askDivination(userId, question, method);
        return Result.success(result);
    }
}
