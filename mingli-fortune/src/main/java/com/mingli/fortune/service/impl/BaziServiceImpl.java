package com.mingli.fortune.service.impl;

import com.alibaba.fastjson2.JSON;
import com.mingli.fortune.domain.FortuneBirthChart;
import com.mingli.fortune.mapper.FortuneBirthChartMapper;
import com.mingli.fortune.service.IBaziService;
import com.mingli.fortune.utils.BaziCalculator;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BaziServiceImpl implements IBaziService {

    @Autowired
    private FortuneBirthChartMapper birthChartMapper;

    @Override
    public Map<String, Object> calculateBazi(Long userId, String birthDate, String birthHour, String gender) {
        // 解析日期 yyyy-MM-dd
        Date date = DateUtil.parse(birthDate);
        int year = DateUtil.year(date);
        int month = DateUtil.month(date) + 1; // hutool month is 0-based
        int day = DateUtil.dayOfMonth(date);

        // 解析时辰 -> 小时数 (子=0, 丑=2, 寅=4, 卯=6...)
        int hour = 12; // 默认午时
        if (birthHour != null && !birthHour.isEmpty()) {
            Map<String, Integer> hourMap = new HashMap<>();
            hourMap.put("子", 0); hourMap.put("丑", 2); hourMap.put("寅", 4);
            hourMap.put("卯", 6); hourMap.put("辰", 8); hourMap.put("巳", 10);
            hourMap.put("午", 12); hourMap.put("未", 14); hourMap.put("申", 16);
            hourMap.put("酉", 18); hourMap.put("戌", 20); hourMap.put("亥", 22);
            hour = hourMap.getOrDefault(birthHour, 12);
        }

        Map<String, Object> result = BaziCalculator.calculateFullBazi(year, month, day, hour,
                gender, birthHour);

        // 保存到数据库
        FortuneBirthChart chart = new FortuneBirthChart();
        chart.setUserId(userId);
        chart.setBirthDate(date);
        chart.setBirthHour(birthHour);
        chart.setGender(gender);
        chart.setYearPillar((String) result.get("yearPillar"));
        chart.setMonthPillar((String) result.get("monthPillar"));
        chart.setDayPillar((String) result.get("dayPillar"));
        chart.setHourPillar((String) result.get("hourPillar"));
        chart.setHeavenlyStems((String) result.get("heavenlyStems"));
        chart.setEarthlyBranches((String) result.get("earthlyBranches"));
        chart.setFiveElements(JSON.toJSONString(result.get("fiveElements")));
        chart.setLuckGods((String) result.get("luckGods"));
        chart.setUnluckGods((String) result.get("unluckGods"));
        chart.setSummary((String) result.get("summary"));
        chart.setCreateTime(new Date());

        birthChartMapper.insert(chart);
        result.put("chartId", chart.getId());

        return result;
    }

    @Override
    public Map<String, Object> getMyChart(Long userId) {
        List<FortuneBirthChart> list = birthChartMapper.selectByUserId(userId);
        if (list == null || list.isEmpty()) return null;
        FortuneBirthChart chart = list.get(0); // 取最新一条（按ID倒序）

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("chartId", chart.getId());
        map.put("birthDate", chart.getBirthDate());
        map.put("birthHour", chart.getBirthHour());
        map.put("gender", chart.getGender());
        map.put("yearPillar", chart.getYearPillar());
        map.put("monthPillar", chart.getMonthPillar());
        map.put("dayPillar", chart.getDayPillar());
        map.put("hourPillar", chart.getHourPillar());
        map.put("heavenlyStems", chart.getHeavenlyStems());
        map.put("earthlyBranches", chart.getEarthlyBranches());
        map.put("fiveElements", chart.getFiveElements());
        map.put("luckGods", chart.getLuckGods());
        map.put("unluckGods", chart.getUnluckGods());
        map.put("summary", chart.getSummary());
        return map;
    }

    @Override
    public List<Map<String, Object>> getHistory(Long userId) {
        List<FortuneBirthChart> list = birthChartMapper.selectByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (FortuneBirthChart c : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", c.getId());
            m.put("birthDate", c.getBirthDate());
            m.put("birthHour", c.getBirthHour());
            m.put("yearPillar", c.getYearPillar());
            m.put("monthPillar", c.getMonthPillar());
            m.put("dayPillar", c.getDayPillar());
            m.put("hourPillar", c.getHourPillar());
            m.put("createTime", c.getCreateTime());
            result.add(m);
        }
        return result;
    }
}
