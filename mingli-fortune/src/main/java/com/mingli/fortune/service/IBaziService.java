package com.mingli.fortune.service;

import java.util.List;
import java.util.Map;

/**
 * 八字算命服务接口
 */
public interface IBaziService {

    /**
     * 计算八字并保存命盘
     */
    Map<String, Object> calculateBazi(Long userId, String birthDate, String birthHour, String gender);

    /**
     * 获取用户的命盘
     */
    Map<String, Object> getMyChart(Long userId);

    /**
     * 获取历史记录
     */
    List<Map<String, Object>> getHistory(Long userId);
}
