package com.mingli.fortune.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.ChineseDate;

import java.util.*;

/**
 * 八字计算工具类
 * 实现天干地支计算、五行分析、喜用神判断等功能
 */
public class BaziCalculator {

    // 天干
    private static final String[] HEAVENLY_STEMS = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    // 地支
    private static final String[] EARTHLY_BRANCHES = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    // 时辰对应地支索引 (23-01=子, 01-03=丑, ...)
    private static final int[] HOUR_BRANCH_MAP = {0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11};

    // 五行对照 - 天干
    private static final Map<String, String> STEM_ELEMENT_MAP = new HashMap<>();
    // 五行对照 - 地支
    private static final Map<String, String> BRANCH_ELEMENT_MAP = new HashMap<>();
    // 五行生克关系
    private static final Map<String, List<String>> ELEMENT_GENERATES = new HashMap<>();
    private static final Map<String, List<String>> ELEMENT_CONTROLS = new HashMap<>();

    static {
        // 天干五行
        STEM_ELEMENT_MAP.put("甲", "木"); STEM_ELEMENT_MAP.put("乙", "木");
        STEM_ELEMENT_MAP.put("丙", "火"); STEM_ELEMENT_MAP.put("丁", "火");
        STEM_ELEMENT_MAP.put("戊", "土"); STEM_ELEMENT_MAP.put("己", "土");
        STEM_ELEMENT_MAP.put("庚", "金"); STEM_ELEMENT_MAP.put("辛", "金");
        STEM_ELEMENT_MAP.put("壬", "水"); STEM_ELEMENT_MAP.put("癸", "水");

        // 地支五行
        BRANCH_ELEMENT_MAP.put("寅", "木"); BRANCH_ELEMENT_MAP.put("卯", "木");
        BRANCH_ELEMENT_MAP.put("巳", "火"); BRANCH_ELEMENT_MAP.put("午", "火");
        BRANCH_ELEMENT_MAP.put("辰", "土"); BRANCH_ELEMENT_MAP.put("戌", "土");
        BRANCH_ELEMENT_MAP.put("丑", "土"); BRANCH_ELEMENT_MAP.put("未", "土");
        BRANCH_ELEMENT_MAP.put("申", "金"); BRANCH_ELEMENT_MAP.put("酉", "金");
        BRANCH_ELEMENT_MAP.put("亥", "水"); BRANCH_ELEMENT_MAP.put("子", "水");

        // 五行相生
        ELEMENT_GENERATES.put("木", Collections.singletonList("火"));
        ELEMENT_GENERATES.put("火", Collections.singletonList("土"));
        ELEMENT_GENERATES.put("土", Collections.singletonList("金"));
        ELEMENT_GENERATES.put("金", Collections.singletonList("水"));
        ELEMENT_GENERATES.put("水", Collections.singletonList("木"));

        // 五行相克
        ELEMENT_CONTROLS.put("木", Collections.singletonList("土"));
        ELEMENT_CONTROLS.put("土", Collections.singletonList("水"));
        ELEMENT_CONTROLS.put("水", Collections.singletonList("火"));
        ELEMENT_CONTROLS.put("火", Collections.singletonList("金"));
        ELEMENT_CONTROLS.put("金", Collections.singletonList("木"));
    }

    /**
     * 获取天干数组
     */
    public static String[] getHeavenlyStems() {
        return HEAVENLY_STEMS;
    }

    /**
     * 获取地支数组
     */
    public static String[] getEarthlyBranches() {
        return EARTHLY_BRANCHES;
    }

    /**
     * 判断是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 计算年柱
     * 立春前按上年算，立春后按本年算
     */
    public static String[] calcYearPillar(int year, int month, int day) {
        // 立春日期（简化处理：2月4日前后）
        int springStartMonth = 2;
        int springStartDay = 4;

        // 如果当前日期在立春之前，则使用上一年
        int actualYear = year;
        if (month < springStartMonth || (month == springStartMonth && day < springStartDay)) {
            actualYear = year - 1;
        }

        int stemIdx = (actualYear - 4) % 10;
        if (stemIdx < 0) stemIdx += 10;
        int branchIdx = (actualYear - 4) % 12;
        if (branchIdx < 0) branchIdx += 12;

        return new String[]{HEAVENLY_STEMS[stemIdx], EARTHLY_BRANCHES[branchIdx], String.valueOf(stemIdx), String.valueOf(branchIdx)};
    }

    /**
     * 计算月柱
     * 根据年份天干和月份推算
     */
    public static String[] calcMonthPillar(int yearStemIdx, int month, int day) {
        // 节气近似日期（简化处理，仅用于月柱计算）
        // 寅月(2月4日-3月5日), 卯月(3月6日-4月4日), ...
        int[][] solarTerms = {
                {2, 4}, {3, 6}, {4, 5}, {5, 6}, {6, 6}, {7, 7},
                {8, 7}, {9, 8}, {10, 8}, {11, 7}, {12, 7}, {1, 6}
        };

        // 确定月份的地支索引 (寅=2, 卯=3, ..., 丑=1)
        int monthBranchIdx;
        if (month == 1) {
            // 1月：小寒(1月6日)前为丑月，后为寅月？不对，按节气：
            // 小寒(1月6日)~立春(2月4日) => 丑月
            // 所以1月6日前为丑月，后也还是丑月直到立春
            // 简化：1月看小寒
            monthBranchIdx = (day < 6) ? 0 : 0; // 丑月索引0... 不对
            // 丑对应地支索引1
            monthBranchIdx = 0; // 子月... 不对
            // 让我重新理一下：
            // 寅(2)月: 立春(2/4)~惊蛰(3/5)
            // 卯(3)月: 惊蛰(3/6)~清明(4/4)
            // 辰(4)月: 清明(4/5)~立夏(5/5)
            // 巳(5)月: 立夏(5/6)~芒种(6/5)
            // 午(6)月: 芒种(6/6)~小暑(7/6)
            // 未(7)月: 小暑(7/7)~立秋(8/6)
            // 申(8)月: 立秋(8/7)~白露(9/7)
            // 酉(9)月: 白露(9/8)~寒露(10/7)
            // 戌(10)月: 寒露(10/8)~立冬(11/6)
            // 亥(11)月: 立冬(11/7)~大雪(12/6)
            // 子(0)月: 大雪(12/7)~小寒(1/5)
            // 丑(1)月: 小寒(1/6)~立春(2/3)

            if (day >= 6) {
                monthBranchIdx = 1; // 丑月
            } else {
                monthBranchIdx = 0; // 子月
            }
        } else if (month == 2) {
            if (day < 4) {
                monthBranchIdx = 1; // 丑月
            } else {
                monthBranchIdx = 2; // 寅月
            }
        } else if (month == 3) {
            monthBranchIdx = (day < 6) ? 2 : 3;
        } else if (month == 4) {
            monthBranchIdx = (day < 5) ? 3 : 4;
        } else if (month == 5) {
            monthBranchIdx = (day < 6) ? 4 : 5;
        } else if (month == 6) {
            monthBranchIdx = (day < 6) ? 5 : 6;
        } else if (month == 7) {
            monthBranchIdx = (day < 7) ? 6 : 7;
        } else if (month == 8) {
            monthBranchIdx = (day < 7) ? 7 : 8;
        } else if (month == 9) {
            monthBranchIdx = (day < 8) ? 8 : 9;
        } else if (month == 10) {
            monthBranchIdx = (day < 8) ? 9 : 10;
        } else if (month == 11) {
            monthBranchIdx = (day < 7) ? 10 : 11;
        } else { // month == 12
            monthBranchIdx = (day < 7) ? 11 : 0;
        }

        // 正月(寅月)为起点，月支偏移量
        // 寅=2, 卯=3, ..., 丑=1
        // monthIndex: 寅月=0, 卯月=1, ..., 丑月=11
        int monthIndex = (monthBranchIdx - 2 + 12) % 12;

        // 月干公式: (年干%5)*2 + 2 + monthIndex
        int firstMonthStemIdx = ((yearStemIdx % 5) * 2 + 2) % 10;
        int stemIdx = (firstMonthStemIdx + monthIndex) % 10;
        int branchIdx = monthBranchIdx;

        return new String[]{HEAVENLY_STEMS[stemIdx], EARTHLY_BRANCHES[branchIdx], String.valueOf(stemIdx), String.valueOf(branchIdx)};
    }

    /**
     * 计算日柱
     * 使用日干支公式（1900-2100年有效）
     */
    public static String[] calcDayPillar(int year, int month, int day) {
        int y = year, m = month, d = day;
        if (m <= 2) {
            y--;
            m += 12;
        }
        int c = y / 100;
        int yy = y % 100;

        // 日干: G = (4C + C/4 + 5y + y/4 + 3*(m+1)/5 + d - 3) mod 10
        int g = (4 * c + c / 4 + 5 * yy + yy / 4 + 3 * (m + 1) / 5 + d - 3) % 10;
        // 日支: Z = (8C + C/4 + 5y + y/4 + 3*(m+1)/5 + d + 7) mod 12
        int z = (8 * c + c / 4 + 5 * yy + yy / 4 + 3 * (m + 1) / 5 + d + 7) % 12;

        int stemIdx = (g % 10 + 10) % 10;
        int branchIdx = (z % 12 + 12) % 12;

        return new String[]{HEAVENLY_STEMS[stemIdx], EARTHLY_BRANCHES[branchIdx], String.valueOf(stemIdx), String.valueOf(branchIdx)};
    }

    /**
     * 计算时柱
     * 根据日干和时辰推算
     */
    public static String[] calcHourPillar(int dayStemIdx, int hour) {
        // 时辰对应的地支索引
        int hourBranchIdx = HOUR_BRANCH_MAP[hour % 24];

        // 时干公式: (日干%5)*2 + 时支索引
        int startStemIdx = (dayStemIdx % 5) * 2;
        int stemIdx = (startStemIdx + hourBranchIdx) % 10;

        return new String[]{HEAVENLY_STEMS[stemIdx], EARTHLY_BRANCHES[hourBranchIdx], String.valueOf(stemIdx), String.valueOf(hourBranchIdx)};
    }

    /**
     * 根据时辰名称获取时辰地支索引
     */
    public static int getHourBranchIndex(String hourName) {
        for (int i = 0; i < EARTHLY_BRANCHES.length; i++) {
            if (EARTHLY_BRANCHES[i].equals(hourName)) {
                return i;
            }
        }
        // 尝试数字解析: 子=0, 丑=1, ...
        return 0;
    }

    /**
     * 时辰名称转地支索引
     */
    public static int convertHourToBranchIndex(String birthHour) {
        if (birthHour == null || birthHour.isEmpty()) return -1;
        String[] hours = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
        for (int i = 0; i < hours.length; i++) {
            if (hours[i].equals(birthHour)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取天干五行
     */
    public static String getStemElement(String stem) {
        return STEM_ELEMENT_MAP.getOrDefault(stem, "");
    }

    /**
     * 获取地支五行
     */
    public static String getBranchElement(String branch) {
        return BRANCH_ELEMENT_MAP.getOrDefault(branch, "");
    }

    /**
     * 分析五行分布
     * 根据四柱天干地支统计五行个数
     */
    public static Map<String, Integer> analyzeFiveElements(String[] yearPillar, String[] monthPillar,
                                                            String[] dayPillar, String[] hourPillar) {
        Map<String, Integer> elementCount = new HashMap<>();
        elementCount.put("木", 0);
        elementCount.put("火", 0);
        elementCount.put("土", 0);
        elementCount.put("金", 0);
        elementCount.put("水", 0);

        String[] stems = {yearPillar[0], monthPillar[0], dayPillar[0], hourPillar[0]};
        String[] branches = {yearPillar[1], monthPillar[1], dayPillar[1], hourPillar[1]};

        // 统计天干五行
        for (String stem : stems) {
            String elem = STEM_ELEMENT_MAP.get(stem);
            if (elem != null) {
                elementCount.put(elem, elementCount.get(elem) + 1);
            }
        }

        // 统计地支五行（每个地支算1个）
        for (String branch : branches) {
            String elem = BRANCH_ELEMENT_MAP.get(branch);
            if (elem != null) {
                elementCount.put(elem, elementCount.get(elem) + 1);
            }
        }

        return elementCount;
    }

    /**
     * 判断喜用神
     * 根据五行强弱，缺什么补什么（简化逻辑）
     */
    public static String[] judgeLuckGods(Map<String, Integer> elementCount) {
        // 找出最弱的五行（出现次数最少）
        int minCount = Integer.MAX_VALUE;
        String minElement = "";
        int maxCount = 0;
        String maxElement = "";

        for (Map.Entry<String, Integer> entry : elementCount.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                minElement = entry.getKey();
            }
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxElement = entry.getKey();
            }
        }

        // 喜用神：缺什么补什么（即最弱的五行）
        // 如果所有五行都有，则取被最旺的五行所生的五行
        String luckGod = minElement;
        String unluckGod = maxElement;

        // 如果有五行缺失（count=0），则补缺失的五行
        for (Map.Entry<String, Integer> entry : elementCount.entrySet()) {
            if (entry.getValue() == 0) {
                luckGod = entry.getKey();
                break;
            }
        }

        // 忌神：被喜用神所克的五行，或克喜用神的五行
        List<String> generated = ELEMENT_GENERATES.get(luckGod);
        if (generated != null && !generated.isEmpty()) {
            unluckGod = generated.get(0);
        }

        return new String[]{luckGod, unluckGod};
    }

    /**
     * 生成命理总评
     */
    public static String generateSummary(Map<String, Integer> elementCount, String[] luckGods) {
        StringBuilder sb = new StringBuilder();
        sb.append("【五行分布】");
        sb.append("木").append(elementCount.get("木")).append("个、");
        sb.append("火").append(elementCount.get("火")).append("个、");
        sb.append("土").append(elementCount.get("土")).append("个、");
        sb.append("金").append(elementCount.get("金")).append("个、");
        sb.append("水").append(elementCount.get("水")).append("个。");

        // 分析五行平衡
        int total = elementCount.values().stream().mapToInt(Integer::intValue).sum();
        int zeroCount = 0;
        for (int v : elementCount.values()) {
            if (v == 0) zeroCount++;
        }

        if (zeroCount >= 3) {
            sb.append("五行偏枯，命局失衡，需后天补益。");
        } else if (zeroCount == 2) {
            sb.append("五行略有不足，可适当调和。");
        } else if (zeroCount == 1) {
            sb.append("五行较为平衡，命局良好。");
        } else {
            sb.append("五行齐全，命格圆满。");
        }

        sb.append("【喜用神】").append(luckGods[0]).append("为喜用神，宜多用与").append(luckGods[0]).append("相关的事物。");
        sb.append("【忌神】").append(luckGods[1]).append("为忌神，宜适当规避。");

        return sb.toString();
    }

    /**
     * 计算完整八字
     */
    public static Map<String, Object> calculateFullBazi(int year, int month, int day, int hour,
                                                         String gender, String birthHour) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 年柱
        String[] yearPillar = calcYearPillar(year, month, day);
        result.put("yearPillar", yearPillar[0] + yearPillar[1]);

        // 月柱
        int yearStemIdx = Integer.parseInt(yearPillar[2]);
        String[] monthPillar = calcMonthPillar(yearStemIdx, month, day);
        result.put("monthPillar", monthPillar[0] + monthPillar[1]);

        // 日柱
        String[] dayPillar = calcDayPillar(year, month, day);
        result.put("dayPillar", dayPillar[0] + dayPillar[1]);

        // 时柱
        String[] hourPillar;
        if (birthHour != null && !birthHour.isEmpty()) {
            int hourBranchIdx = convertHourToBranchIndex(birthHour);
            if (hourBranchIdx >= 0) {
                int dayStemIdx = Integer.parseInt(dayPillar[2]);
                int startStemIdx = (dayStemIdx % 5) * 2;
                int stemIdx = (startStemIdx + hourBranchIdx) % 10;
                hourPillar = new String[]{HEAVENLY_STEMS[stemIdx], EARTHLY_BRANCHES[hourBranchIdx],
                        String.valueOf(stemIdx), String.valueOf(hourBranchIdx)};
            } else {
                hourPillar = calcHourPillar(Integer.parseInt(dayPillar[2]), hour);
            }
        } else {
            hourPillar = calcHourPillar(Integer.parseInt(dayPillar[2]), hour);
        }
        result.put("hourPillar", hourPillar[0] + hourPillar[1]);

        // 天干地支
        String heavenlyStems = yearPillar[0] + monthPillar[0] + dayPillar[0] + hourPillar[0];
        String earthlyBranches = yearPillar[1] + monthPillar[1] + dayPillar[1] + hourPillar[1];
        result.put("heavenlyStems", heavenlyStems);
        result.put("earthlyBranches", earthlyBranches);

        // 四柱完整信息
        result.put("yearStem", yearPillar[0]);
        result.put("yearBranch", yearPillar[1]);
        result.put("monthStem", monthPillar[0]);
        result.put("monthBranch", monthPillar[1]);
        result.put("dayStem", dayPillar[0]);
        result.put("dayBranch", dayPillar[1]);
        result.put("hourStem", hourPillar[0]);
        result.put("hourBranch", hourPillar[1]);

        // 五行分析
        Map<String, Integer> elementCount = analyzeFiveElements(yearPillar, monthPillar, dayPillar, hourPillar);
        result.put("fiveElements", elementCount);

        // 喜用神和忌神
        String[] luckGods = judgeLuckGods(elementCount);
        result.put("luckGods", luckGods[0]);
        result.put("unluckGods", luckGods[1]);

        // 命理总评
        String summary = generateSummary(elementCount, luckGods);
        result.put("summary", summary);

        return result;
    }
}
