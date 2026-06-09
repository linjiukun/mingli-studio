package com.mingli.fortune.service;

import java.util.List;
import java.util.Map;

/**
 * 星座运势服务接口
 */
public interface IZodiacService {

    /**
     * 获取用户星座（根据用户出生日期）
     */
    Map<String, Object> getMyZodiac(Long userId);

    /**
     * 获取星座详情
     */
    Map<String, Object> getZodiacSign(int index);

    /**
     * 获取所有星座列表
     */
    List<Map<String, Object>> getAllZodiacSigns();

    /**
     * 获取星座每日运势
     */
    Map<String, Object> getDailyHoroscope(int zodiacIndex, String date);

    /**
     * 获取星座周运势
     */
    Map<String, Object> getWeeklyHoroscope(int zodiacIndex, String week);

    /**
     * 获取星座月运势
     */
    Map<String, Object> getMonthlyHoroscope(int zodiacIndex, String month);

    /**
     * 星座配对分析
     */
    Map<String, Object> getCompatibility(int sign1, int sign2);

    /**
     * 计算指定生日的星座
     */
    Map<String, Object> calculateZodiac(int month, int day);
}
