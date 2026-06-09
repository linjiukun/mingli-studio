package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.service.IZodiacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 十二星座运势控制器
 */
@RestController
@RequestMapping("/api/zodiac")
public class ZodiacController {

    @Autowired
    private IZodiacService zodiacService;

    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }

    /**
     * 获取我的星座（根据用户出生日期）
     */
    @GetMapping("/my-sign")
    public Result mySign(HttpServletRequest request) {
        Long userId = getUserId(request);
        Map<String, Object> result = zodiacService.getMyZodiac(userId);
        if (result.containsKey("error")) {
            return Result.error((String) result.get("error"));
        }
        return Result.success(result);
    }

    /**
     * 获取所有星座列表
     */
    @GetMapping("/all-signs")
    public Result allSigns() {
        List<Map<String, Object>> list = zodiacService.getAllZodiacSigns();
        return Result.success(list);
    }

    /**
     * 获取星座详情
     */
    @GetMapping("/sign/{index}")
    public Result signDetail(@PathVariable Integer index) {
        if (index < 0 || index > 11) {
            return Result.error("星座索引无效，取值范围 0-11");
        }
        Map<String, Object> result = zodiacService.getZodiacSign(index);
        return Result.success(result);
    }

    /**
     * 根据生日计算星座
     */
    @GetMapping("/calculate")
    public Result calculate(@RequestParam Integer month, @RequestParam Integer day) {
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return Result.error("日期参数无效");
        }
        Map<String, Object> result = zodiacService.calculateZodiac(month, day);
        if (result.containsKey("error")) {
            return Result.error((String) result.get("error"));
        }
        return Result.success(result);
    }

    /**
     * 获取星座每日运势
     */
    @GetMapping("/daily")
    public Result dailyHoroscope(@RequestParam Integer sign,
                                  @RequestParam(required = false) String date) {
        if (sign < 0 || sign > 11) {
            return Result.error("星座索引无效");
        }
        Map<String, Object> result = zodiacService.getDailyHoroscope(sign, date);
        return Result.success(result);
    }

    /**
     * 获取星座周运势
     */
    @GetMapping("/weekly")
    public Result weeklyHoroscope(@RequestParam Integer sign,
                                   @RequestParam(required = false) String week) {
        if (sign < 0 || sign > 11) {
            return Result.error("星座索引无效");
        }
        Map<String, Object> result = zodiacService.getWeeklyHoroscope(sign, week);
        return Result.success(result);
    }

    /**
     * 获取星座月运势
     */
    @GetMapping("/monthly")
    public Result monthlyHoroscope(@RequestParam Integer sign,
                                    @RequestParam(required = false) String month) {
        if (sign < 0 || sign > 11) {
            return Result.error("星座索引无效");
        }
        Map<String, Object> result = zodiacService.getMonthlyHoroscope(sign, month);
        return Result.success(result);
    }

    /**
     * 星座配对分析
     */
    @GetMapping("/compatibility")
    public Result compatibility(@RequestParam Integer sign1, @RequestParam Integer sign2) {
        if (sign1 < 0 || sign1 > 11 || sign2 < 0 || sign2 > 11) {
            return Result.error("星座索引无效");
        }
        Map<String, Object> result = zodiacService.getCompatibility(sign1, sign2);
        return Result.success(result);
    }
}
