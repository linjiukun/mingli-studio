package com.mingli.fortune.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mingli.fortune.domain.FortuneDaily;
import com.mingli.fortune.mapper.FortuneDailyMapper;
import com.mingli.fortune.service.IDailyFortuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DailyFortuneServiceImpl implements IDailyFortuneService {

    @Autowired
    private FortuneDailyMapper dailyMapper;

    @Override
    public Map<String, Object> getTodayFortune(Long userId) {
        String today = DateUtil.today();
        Date todayDate = DateUtil.parseDate(today);
        FortuneDaily daily = dailyMapper.selectByUserAndDate(userId, todayDate);
        if (daily == null) {
            // 自动生成今日运势
            daily = generateDailyFortune(userId, today);
        }
        return toMap(daily);
    }

    @Override
    public List<Map<String, Object>> getRangeFortune(Long userId, String startDate, String endDate) {
        Date start = DateUtil.parseDate(startDate);
        Date end = DateUtil.parseDate(endDate);
        List<FortuneDaily> list = dailyMapper.selectByUserAndDateRange(userId, start, end);
        List<Map<String, Object>> result = new ArrayList<>();
        for (FortuneDaily d : list) {
            result.add(toMap(d));
        }
        return result;
    }

    private FortuneDaily generateDailyFortune(Long userId, String dateStr) {
        // 基于日期和用户ID生成伪随机运势
        Date date = DateUtil.parseDate(dateStr);
        int seed = (userId.toString() + dateStr).hashCode();
        Random rand = new Random(seed);

        FortuneDaily d = new FortuneDaily();
        d.setUserId(userId);
        d.setFortuneDate(date);
        d.setOverallScore(40 + rand.nextInt(61));
        d.setWealthScore(30 + rand.nextInt(71));
        d.setLoveScore(30 + rand.nextInt(71));
        d.setCareerScore(30 + rand.nextInt(71));
        d.setHealthScore(40 + rand.nextInt(61));

        String[] luckyColors = {"红色", "金色", "蓝色", "绿色", "紫色", "白色", "黑色", "黄色"};
        d.setLuckyColor(luckyColors[rand.nextInt(luckyColors.length)]);

        d.setLuckyNumber(String.valueOf(rand.nextInt(99) + 1));

        String[] directions = {"东", "南", "西", "北", "东南", "西南", "东北", "西北"};
        d.setLuckyDirection(directions[rand.nextInt(directions.length)]);
        d.setAvoidDirection(directions[rand.nextInt(directions.length)]);

        String[] advices = {
                "宜静思，不宜冲动决策",
                "今日宜结交贵人，拓展人脉",
                "保持耐心，好事多磨",
                "注意财务管理，避免不必要开支",
                "适合学习新知识，提升自我",
                "宜运动，注意调节身心",
                "宜主动出击，把握机会",
                "宜低调行事，韬光养晦"
        };
        d.setAdvice(advices[rand.nextInt(advices.length)]);
        d.setCreateTime(new Date());

        dailyMapper.insert(d);
        return d;
    }

    private Map<String, Object> toMap(FortuneDaily d) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", d.getId());
        map.put("fortuneDate", d.getFortuneDate());
        map.put("overallScore", d.getOverallScore());
        map.put("wealthScore", d.getWealthScore());
        map.put("loveScore", d.getLoveScore());
        map.put("careerScore", d.getCareerScore());
        map.put("healthScore", d.getHealthScore());
        map.put("luckyColor", d.getLuckyColor());
        map.put("luckyNumber", d.getLuckyNumber());
        map.put("luckyDirection", d.getLuckyDirection());
        map.put("avoidDirection", d.getAvoidDirection());
        map.put("advice", d.getAdvice());
        return map;
    }
}
