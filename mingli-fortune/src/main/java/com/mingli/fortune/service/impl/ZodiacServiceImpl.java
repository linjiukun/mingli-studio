package com.mingli.fortune.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mingli.fortune.service.IZodiacService;
import com.mingli.fortune.utils.ZodiacCalculator;
import com.mingli.fortune.utils.ZodiacCalculator.ZodiacInfo;
import com.mingli.system.domain.SysUser;
import com.mingli.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 星座运势服务实现
 */
@Service
public class ZodiacServiceImpl implements IZodiacService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public Map<String, Object> getMyZodiac(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || user.getBirthDate() == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("error", "用户信息或出生日期未设置");
            return result;
        }

        java.util.Date birthDate = user.getBirthDate();
        String dateStr = DateUtil.formatDate(birthDate);
        int index = ZodiacCalculator.getZodiacIndexFromBirthday(dateStr);
        return getZodiacSign(index);
    }

    @Override
    public Map<String, Object> getZodiacSign(int index) {
        if (index < 0 || index > 11) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("error", "无效的星座索引: " + index);
            return err;
        }
        ZodiacInfo info = ZodiacCalculator.getZodiacInfo(index);
        return zodiacInfoToMap(info);
    }

    @Override
    public List<Map<String, Object>> getAllZodiacSigns() {
        List<ZodiacInfo> list = ZodiacCalculator.getAllZodiacSigns();
        List<Map<String, Object>> result = new ArrayList<>();
        for (ZodiacInfo info : list) {
            result.add(zodiacInfoToMap(info));
        }
        return result;
    }

    @Override
    public Map<String, Object> getDailyHoroscope(int zodiacIndex, String date) {
        if (date == null || date.isEmpty()) {
            date = DateUtil.today();
        }
        return ZodiacCalculator.generateDailyHoroscope(zodiacIndex, date);
    }

    @Override
    public Map<String, Object> getWeeklyHoroscope(int zodiacIndex, String week) {
        if (week == null || week.isEmpty()) {
            // 使用当前周标识
            week = DateUtil.today() + "-week";
        }
        return ZodiacCalculator.generateWeeklyHoroscope(zodiacIndex, week);
    }

    @Override
    public Map<String, Object> getMonthlyHoroscope(int zodiacIndex, String month) {
        if (month == null || month.isEmpty()) {
            month = DateUtil.format(DateUtil.date(), "yyyy-MM");
        }
        return ZodiacCalculator.generateMonthlyHoroscope(zodiacIndex, month);
    }

    @Override
    public Map<String, Object> getCompatibility(int sign1, int sign2) {
        return ZodiacCalculator.getCompatibility(sign1, sign2);
    }

    @Override
    public Map<String, Object> calculateZodiac(int month, int day) {
        int index = ZodiacCalculator.getZodiacIndex(month, day);
        ZodiacInfo info = ZodiacCalculator.getZodiacInfo(index);
        Map<String, Object> result = zodiacInfoToMap(info);
        result.put("inputMonth", month);
        result.put("inputDay", day);
        return result;
    }

    private Map<String, Object> zodiacInfoToMap(ZodiacInfo info) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("index", info.index);
        map.put("nameCn", info.nameCn);
        map.put("nameEn", info.nameEn);
        map.put("unicode", info.unicode);
        map.put("dateRange", info.dateRange);
        map.put("element", info.element);
        map.put("rulingPlanet", info.rulingPlanet);
        map.put("luckyNumbers", info.luckyNumbers);
        map.put("luckyColors", info.luckyColors);
        map.put("traits", info.traits);
        map.put("description", info.description);
        map.put("bestMatch", info.bestMatch);
        map.put("worstMatch", info.worstMatch);
        return map;
    }
}
