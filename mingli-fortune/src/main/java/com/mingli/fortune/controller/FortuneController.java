package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.service.IFortuneReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fortune")
public class FortuneController {

    @Autowired
    private IFortuneReadingService readingService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    @PostMapping("/reading")
    public Result requestReading(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = getUserId(request);
        Long chartId = params.get("chartId") != null ? Long.valueOf(params.get("chartId").toString()) : null;
        String readingType = (String) params.get("readingType");
        Map<String, Object> result = readingService.requestReading(userId, chartId, readingType);
        return Result.success(result);
    }

    @GetMapping("/readings")
    public Result readings(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<Map<String, Object>> list = readingService.getReadings(userId);
        return Result.success(list);
    }
}
