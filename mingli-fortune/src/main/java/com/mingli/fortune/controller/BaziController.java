package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.service.IBaziService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bazi")
public class BaziController {

    @Autowired
    private IBaziService baziService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    @PostMapping("/calculate")
    public Result calculate(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = getUserId(request);
        String birthDate = params.get("birthDate");
        String birthHour = params.get("birthHour");
        String gender = params.get("gender");
        Map<String, Object> result = baziService.calculateBazi(userId, birthDate, birthHour, gender);
        return Result.success(result);
    }

    @GetMapping("/my-chart")
    public Result myChart(HttpServletRequest request) {
        Long userId = getUserId(request);
        Map<String, Object> chart = baziService.getMyChart(userId);
        if (chart == null) return Result.error("尚未测算八字");
        return Result.success(chart);
    }

    @GetMapping("/history")
    public Result history(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<Map<String, Object>> list = baziService.getHistory(userId);
        return Result.success(list);
    }
}
