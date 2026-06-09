package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.FortuneBirthChart;
import com.mingli.fortune.domain.FortuneReading;
import com.mingli.fortune.mapper.FortuneBirthChartMapper;
import com.mingli.fortune.mapper.FortuneReadingMapper;
import com.mingli.fortune.service.IFortuneReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FortuneReadingServiceImpl implements IFortuneReadingService {

    @Autowired
    private FortuneReadingMapper readingMapper;

    @Autowired
    private FortuneBirthChartMapper chartMapper;

    @Override
    public Map<String, Object> requestReading(Long userId, Long chartId, String readingType) {
        FortuneBirthChart chart = null;
        if (chartId != null) {
            // 查找指定chart
            chart = chartMapper.selectById(chartId);
        }
        if (chart == null) {
            // 取最新的
            List<FortuneBirthChart> charts = chartMapper.selectByUserId(userId);
            if (charts != null && !charts.isEmpty()) {
                chart = charts.get(0);
            }
        }

        if (chart == null) {
            throw new RuntimeException("请先测算八字");
        }

        // 生成解读内容
        String content = generateReadingContent(chart, readingType);

        // 生成评分
        Random rand = new Random(userId + readingType.hashCode() + System.currentTimeMillis() / 86400000);
        int score = 40 + rand.nextInt(61);

        // 保存
        FortuneReading reading = new FortuneReading();
        reading.setUserId(userId);
        reading.setChartId(chart.getId());
        reading.setReadingType(readingType);
        reading.setContent(content);
        reading.setScore(score);
        reading.setCreateTime(new Date());
        readingMapper.insert(reading);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", reading.getId());
        result.put("readingType", readingType);
        result.put("content", content);
        result.put("score", score);
        result.put("createTime", reading.getCreateTime());
        return result;
    }

    @Override
    public List<Map<String, Object>> getReadings(Long userId) {
        List<FortuneReading> list = readingMapper.selectByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (FortuneReading r : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getId());
            m.put("readingType", r.getReadingType());
            m.put("content", r.getContent());
            m.put("score", r.getScore());
            m.put("createTime", r.getCreateTime());
            result.add(m);
        }
        return result;
    }

    private String generateReadingContent(FortuneBirthChart chart, String readingType) {
        String summary = chart.getSummary() != null ? chart.getSummary() : "命局平衡";
        String yearPillar = chart.getYearPillar() != null ? chart.getYearPillar() : "未知";
        String dayPillar = chart.getDayPillar() != null ? chart.getDayPillar() : "未知";

        StringBuilder sb = new StringBuilder();
        sb.append("【八字命盘】").append(yearPillar).append(" ").append(chart.getMonthPillar())
                .append(" ").append(dayPillar).append(" ").append(chart.getHourPillar()).append("\n\n");

        switch (readingType) {
            case "wealth":
                sb.append("【财运分析】\n");
                sb.append("阁下命造").append(dayPillar).append("为日主，");
                sb.append("财星为养命之源。从命局来看，");
                sb.append(summary.contains("五行平衡") ? "财运平稳，守成有余，进取亦可。"
                        : "财运波动较大，需审时度势，不宜投机取巧。");
                sb.append("\n\n建议：正财宜稳，偏财勿贪。春夏之交运势较佳，可把握良机。");
                break;
            case "marriage":
                sb.append("【婚姻情感】\n");
                sb.append("男以财为妻，女以官为夫。阁下日柱").append(dayPillar);
                sb.append("为夫妻宫之位，");
                sb.append("婚姻运势中平，需用心经营。");
                if ("0".equals(chart.getGender())) {
                    sb.append("男命财星为用，宜择温婉贤淑之配。");
                } else {
                    sb.append("女命官星为用，宜择稳重可靠之伴。");
                }
                sb.append("\n\n建议：坦诚沟通为感情之本，相互理解可化解矛盾。");
                break;
            case "career":
                sb.append("【事业前程】\n");
                sb.append("阁下命局").append(summary);
                sb.append("官杀为事业之星，印星为贵人助力。");
                sb.append("\n\n事业发展宜顺势而为，" +
                        "适合从事与自身五行互补之行业。木火通明者宜创意文化，" +
                        "金水相生者宜金融贸易，土厚载物者宜地产建筑。");
                break;
            default:
                sb.append("【总体运势】\n");
                sb.append(summary);
                sb.append("\n\n阁下日主").append(dayPillar.substring(0, 1))
                        .append("之命，一生运势起伏，贵在把握时机。");
                sb.append(chart.getLuckGods() != null ?
                        "喜用神为" + chart.getLuckGods() + "，宜多接触相关五行之事。" : "");
                break;
        }

        return sb.toString();
    }
}
