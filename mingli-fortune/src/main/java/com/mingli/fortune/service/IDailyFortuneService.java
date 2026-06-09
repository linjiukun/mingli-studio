package com.mingli.fortune.service;

import java.util.List;
import java.util.Map;

/**
 * 每日运势服务接口
 */
public interface IDailyFortuneService {

    /**
     * 获取今日运势
     */
    Map<String, Object> getTodayFortune(Long userId);

    /**
     * 获取指定日期范围的运势
     */
    List<Map<String, Object>> getRangeFortune(Long userId, String startDate, String endDate);
}
