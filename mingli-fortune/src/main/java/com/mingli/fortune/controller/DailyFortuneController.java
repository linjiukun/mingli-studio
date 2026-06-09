package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.service.IDailyFortuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daily")
public class DailyFortuneController {

    @Autowired
    private IDailyFortuneService dailyFortuneService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    @GetMapping("/today")
    public Result today(HttpServletRequest request) {
        Long userId = getUserId(request);
        Map<String, Object> fortune = dailyFortuneService.getTodayFortune(userId);
        return Result.success(fortune);
    }

    @GetMapping("/range")
    public Result range(@RequestParam String startDate, @RequestParam String endDate, HttpServletRequest request) {
        Long userId = getUserId(request);
        List<Map<String, Object>> list = dailyFortuneService.getRangeFortune(userId, startDate, endDate);
        return Result.success(list);
    }
}
