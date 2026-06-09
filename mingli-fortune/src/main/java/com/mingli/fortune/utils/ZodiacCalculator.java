package com.mingli.fortune.utils;

import java.util.*;

/**
 * 十二星座测算核心算法
 * 包含星座定义、日期计算、每日运势生成、配对分析
 */
public class ZodiacCalculator {

    // ======================== 星座基础定义 ========================

    public static final String[] ZODIAC_NAMES_CN = {
            "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座",
            "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"
    };

    public static final String[] ZODIAC_NAMES_EN = {
            "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
            "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };

    public static final String[] ZODIAC_UNICODE = {
            "♈", "♉", "♊", "♋", "♌", "♍",
            "♎", "♏", "♐", "♑", "♒", "♓"
    };

    // 星座日期范围 [月, 日] 起始
    private static final int[][] ZODIAC_START = {
            {3, 21},  // 白羊
            {4, 20},  // 金牛
            {5, 21},  // 双子
            {6, 22},  // 巨蟹
            {7, 23},  // 狮子
            {8, 23},  // 处女
            {9, 23},  // 天秤
            {10, 24}, // 天蝎
            {11, 23}, // 射手
            {12, 22}, // 摩羯
            {1, 20},  // 水瓶
            {2, 19}   // 双鱼
    };

    // 星座属性：五行元素
    public static final String[] ELEMENTS = {
            "火", "土", "风", "水", "火", "土",
            "风", "水", "火", "土", "风", "水"
    };

    // 主宰星
    public static final String[] RULING_PLANETS = {
            "火星", "金星", "水星", "月亮", "太阳", "水星",
            "金星", "冥王星", "木星", "土星", "天王星", "海王星"
    };

    // 幸运数字
    public static final int[][] LUCKY_NUMBERS = {
            {1, 9},    // 白羊
            {2, 6},    // 金牛
            {3, 5},    // 双子
            {2, 7},    // 巨蟹
            {1, 5},    // 狮子
            {5, 8},    // 处女
            {4, 6},    // 天秤
            {2, 8},    // 天蝎
            {3, 9},    // 射手
            {1, 8},    // 摩羯
            {3, 7},    // 水瓶
            {2, 6}     // 双鱼
    };

    // 幸运颜色
    public static final String[][] LUCKY_COLORS = {
            {"红色", "金色"},       // 白羊
            {"粉色", "绿色"},       // 金牛
            {"黄色", "蓝色"},       // 双子
            {"银色", "白色"},       // 巨蟹
            {"金色", "橙色"},       // 狮子
            {"灰色", "米色"},       // 处女
            {"蓝色", "粉色"},       // 天秤
            {"深红", "黑色"},       // 天蝎
            {"紫色", "蓝色"},       // 射手
            {"棕色", "黑色"},       // 摩羯
            {"天蓝", "紫色"},       // 水瓶
            {"海蓝", "紫色"}        // 双鱼
    };

    // 性格特征
    public static final String[][] PERSONALITY_TRAITS = {
            {"热情", "勇敢", "冲动", "直率", "自信", "好胜"},
            {"稳重", "务实", "固执", "温和", "耐心", "享乐"},
            {"机智", "善变", "好奇", "活泼", "善交际", "双重性格"},
            {"敏感", "体贴", "念旧", "保护欲", "情绪化", "直觉强"},
            {"自信", "慷慨", "骄傲", "领导力", "创造力", "热情"},
            {"细致", "完美主义", "理性", "谨慎", "善良", "挑剔"},
            {"优雅", "公正", "犹豫", "社交达人", "追求和谐", "善辩"},
            {"深沉", "执着", "神秘", "洞察力", "意志强", "嫉妒"},
            {"乐观", "自由", "直率", "冒险精神", "幽默", "粗心"},
            {"踏实", "责任心", "保守", "坚韧", "务实", "悲观"},
            {"创新", "独立", "友善", "叛逆", "理性", "不可预测"},
            {"梦幻", "善良", "浪漫", "直觉", "逃避", "牺牲"}
    };

    // 星座配对兼容性矩阵 (0-100分)
    private static final int[][] COMPATIBILITY = {
            // 白 金 双 巨 狮 处 天 天 射 摩 水 双
            {  80, 50, 75, 40, 90, 45, 85, 45, 95, 40, 70, 50 },  // 白羊
            {  50, 80, 45, 85, 40, 90, 55, 70, 45, 85, 40, 75 },  // 金牛
            {  75, 45, 70, 50, 80, 40, 90, 45, 85, 40, 95, 55 },  // 双子
            {  40, 85, 50, 80, 45, 85, 55, 90, 40, 75, 50, 95 },  // 巨蟹
            {  90, 40, 80, 45, 75, 50, 85, 55, 95, 40, 85, 50 },  // 狮子
            {  45, 90, 40, 85, 50, 75, 55, 80, 45, 85, 40, 80 },  // 处女
            {  85, 55, 90, 55, 85, 55, 70, 50, 80, 45, 95, 40 },  // 天秤
            {  45, 70, 45, 90, 55, 80, 50, 80, 50, 85, 45, 85 },  // 天蝎
            {  95, 45, 85, 40, 95, 45, 80, 50, 75, 45, 90, 50 },  // 射手
            {  40, 85, 40, 75, 40, 85, 45, 85, 45, 80, 50, 75 },  // 摩羯
            {  70, 40, 95, 50, 85, 40, 95, 45, 90, 50, 75, 55 },  // 水瓶
            {  50, 75, 55, 95, 50, 80, 40, 85, 50, 75, 55, 80 }   // 双鱼
    };

    // ======================== 星座详情 ========================

    public static class ZodiacInfo {
        public int index;
        public String nameCn;
        public String nameEn;
        public String unicode;
        public String dateRange;
        public String element;
        public String rulingPlanet;
        public int[] luckyNumbers;
        public String[] luckyColors;
        public String[] traits;
        public String description;
        public int[] bestMatch;
        public int[] worstMatch;
    }

    private static final String[] DESCRIPTIONS = {
            "白羊座热情冲动，富有冒险精神，是天生的领导者。他们行动力强，勇往直前，但有时过于急躁。",
            "金牛座稳重务实，对美和物质有着敏锐的感知。他们耐心坚韧，一旦确定目标就会坚持到底。",
            "双子座机智多变，好奇心旺盛，善于沟通交流。他们适应力强，但有双重性格倾向。",
            "巨蟹座温柔敏感，家庭观念强，具有强烈的保护欲。他们直觉敏锐，情感丰富且念旧。",
            "狮子座自信大方，天生具有领袖气质和创造力。他们慷慨热情，渴望被关注和赞赏。",
            "处女座细致完美，追求卓越，理性谨慎。他们善良务实，但有时过于挑剔和焦虑。",
            "天秤座优雅公正，追求和谐，社交能力强。他们善于权衡，但有时犹豫不决。",
            "天蝎座深沉执着，洞察力惊人，意志力极强。他们神秘而专注，爱憎分明。",
            "射手座乐观自由，热爱冒险和探索。他们幽默直率，追求真理和人生意义。",
            "摩羯座踏实坚韧，有强烈的责任感和事业心。他们务实稳重，是值得信赖的伙伴。",
            "水瓶座创新独立，思想前卫，富有人道主义精神。他们友善理性，不受传统约束。",
            "双鱼座梦幻浪漫，极具艺术天赋和同理心。他们善良温柔，但有时缺乏现实感。"
    };

    // 最佳配对
    private static final int[][] BEST_MATCH = {
            {4, 8},    // 白羊最佳：狮子、射手
            {3, 5},    // 金牛最佳：巨蟹、处女
            {6, 10},   // 双子最佳：天秤、水瓶
            {1, 7},    // 巨蟹最佳：金牛、天蝎
            {0, 8},    // 狮子最佳：白羊、射手
            {1, 3},    // 处女最佳：金牛、巨蟹
            {0, 10},   // 天秤最佳：白羊、水瓶
            {3, 11},   // 天蝎最佳：巨蟹、双鱼
            {0, 4},    // 射手最佳：白羊、狮子
            {1, 5},    // 摩羯最佳：金牛、处女
            {6, 8},    // 水瓶最佳：天秤、射手
            {7, 3}     // 双鱼最佳：天蝎、巨蟹
    };

    // 最差配对
    private static final int[][] WORST_MATCH = {
            {5, 9},    // 白羊最差：处女、摩羯
            {8, 0},    // 金牛最差：射手、白羊
            {7, 1},    // 双子最差：天蝎、金牛
            {4, 8},    // 巨蟹最差：狮子、射手
            {3, 9},    // 狮子最差：巨蟹、摩羯
            {4, 8},    // 处女最差：狮子、射手
            {3, 11},   // 天秤最差：巨蟹、双鱼
            {6, 0},    // 天蝎最差：天秤、白羊
            {5, 1},    // 射手最差：处女、金牛
            {4, 0},    // 摩羯最差：狮子、白羊
            {7, 1},    // 水瓶最差：天蝎、金牛
            {5, 0}     // 双鱼最差：处女、白羊
    };

    // 每日运势模板
    private static final String[][] DAILY_FORTUNE_TEMPLATES = {
            {   // 整体运势
                    "今天能量满满，适合主动出击，抓住机会展现自我。",
                    "运势平稳，适合处理日常事务，不宜冒进。",
                    "今日略有不顺，建议放慢节奏，多关注自身感受。",
                    "好运来袭！把握良机，大胆追求目标。",
                    "能量波动较大，注意调节情绪，避免冲动决策。",
                    "今日适合社交，人际关系会带来惊喜。",
                    "运势上升期，之前的努力开始显现成果。",
                    "宜静不宜动，适合独处和反思。",
                    "财运不错，但需谨慎投资，切忌贪心。",
                    "桃花运旺盛，单身者有望遇到心仪对象。"
            },
            {   // 事业
                    "工作运势佳，效率高，适合推进重要项目。",
                    "职场沟通顺畅，合作愉快。",
                    "注意细节，避免因粗心犯错。",
                    "领导力显现，适合带领团队。",
                    "工作中会有新机会出现，积极把握。",
                    "适合学习新技能，提升职场竞争力。",
                    "职场人际关系和谐，同事关系融洽。",
                    "注意职场小人，重要决定多做备份。",
                    "工作压力较大，适当放松更有利于发挥。",
                    "项目进展顺利，有望提前完成目标。"
            },
            {   // 财运
                    "正财运佳，工资收入有望提升。",
                    "偏财运旺，适合小试手气。",
                    "财运平稳，适合做长期理财规划。",
                    "注意消费冲动，避免不必要的开支。",
                    "有意外之财的可能，但不宜投机。",
                    "财运上升，之前的投资有望回报。",
                    "适合学习理财知识，为未来做准备。",
                    "财运一般，不宜进行大额投资。",
                    "收支平衡，适合制定储蓄计划。",
                    "财运旺盛，但需合理分配资金。"
            },
            {   // 感情
                    "桃花运旺盛，魅力四射，吸引众多目光。",
                    "与伴侣关系和谐，适合深入沟通。",
                    "感情运势平稳，享受平淡的幸福。",
                    "注意沟通方式，避免因小事争吵。",
                    "单身者有机会在社交场合遇见有缘人。",
                    "感情升温，适合约会和浪漫活动。",
                    "旧情复燃的可能，但需理性判断。",
                    "感情中需要更多包容和理解。",
                    "桃花运不错，但需擦亮眼睛辨别真心。",
                    "与伴侣共同规划未来，增进感情。"
            },
            {   // 健康
                    "身体健康，精力充沛，适合运动锻炼。",
                    "注意作息规律，避免熬夜。",
                    "换季期间注意预防感冒。",
                    "精神状态良好，心情愉悦。",
                    "适合开始新的健身计划。",
                    "注意饮食健康，规律用餐。",
                    "身体有些疲劳，需要充分休息。",
                    "适合户外活动，呼吸新鲜空气。",
                    "注意肩颈和腰椎健康。",
                    "身心状态俱佳，保持良好习惯。"
            }
    };

    // 周运势模板
    private static final String[] WEEKLY_TEMPLATES = {
            "本周整体运势上扬，前三天适合规划，后四天宜行动。事业上有贵人相助，财运稳中有升。感情方面需多些耐心。",
            "本周运势波动较大，周中会有重要转折。建议保持灵活应变的心态，财运不错但需理性消费。健康方面注意休息。",
            "本周是积累能量的一周。工作上适合做长远规划，财运平顺。感情方面有惊喜，单身者值得期待。",
            "本周运势旺盛！各方面都有不错的表现。事业上会有突破性进展，财运亨通。感情甜蜜，家庭和睦。",
            "本周运势略显低迷，建议保持低调。工作上面临挑战但能克服，财运一般不宜投资。感情方面需要多沟通。",
            "本周贵人运强，事业上会得到重要帮助。财运稳中有升，适合理财规划。感情运佳，适合表达心意。",
            "本周充满了机遇与挑战。工作上需要果断决策，财运旺盛但需谨慎。感情方面桃花运不错。",
            "本周宜稳扎稳打，不宜冒险。工作按部就班也能出成绩，财运平稳。感情方面适合陪伴家人。"
    };

    // 月运势模板
    private static final String[] MONTHLY_TEMPLATES = {
            "本月是充满活力的一月。事业上有多个机会出现，财运整体向好。感情生活丰富多彩。建议把握月初的好时机。",
            "本月运势稳步上升。前半月适合积累和规划，后半月适合执行和冲刺。财运逐渐好转，投资有回报。",
            "本月需要注意节奏的把控。工作压力与机遇并存，财运先抑后扬。感情方面沟通是关键。",
            "本月是收获的季节。之前付出的努力开始看到成果，事业稳步前进。财运佳，适合做重要决策。",
            "本月运势旺盛！事业上会有重要突破，贵人运强。财运亨通，感情甜蜜。是充满成就感的一个月。",
            "本月需要更多耐心和细心。工作上细节决定成败，财运平稳。感情方面慢下来感受美好。",
            "本月变化较多，需要灵活应对。事业上有新的方向出现，财运不错但需谨慎。感情方面有望升温。",
            "本月运势先抑后扬。上半月需要坚持，下半月迎来转机。财运逐渐好转，感情运势佳。"
    };

    // 建议模板
    private static final String[] ADVICE_TEMPLATES = {
            "把握当下，勇敢追逐梦想。",
            "保持耐心，好事总会发生。",
            "多与人交流，会有意想不到的收获。",
            "注意倾听内心的声音。",
            "适时的放松是为了更好地出发。",
            "相信自己的直觉，它不会骗你。",
            "今天的好运来自于昨天的努力。",
            "珍惜身边的人，他们是你最大的财富。",
            "保持开放的心态，接纳不同的可能。",
            "先照顾好自己，才能更好地照顾他人。"
    };

    // ======================== 核心算法 ========================

    /**
     * 根据出生日期计算星座索引 (0-11)
     */
    public static int getZodiacIndex(int month, int day) {
        if (!isValidMonthDay(month, day)) {
            return -1;
        }

        int monthDay = month * 100 + day;
        if (monthDay >= 321 && monthDay <= 419) return 0;    // 白羊座
        if (monthDay >= 420 && monthDay <= 520) return 1;    // 金牛座
        if (monthDay >= 521 && monthDay <= 621) return 2;    // 双子座
        if (monthDay >= 622 && monthDay <= 722) return 3;    // 巨蟹座
        if (monthDay >= 723 && monthDay <= 822) return 4;    // 狮子座
        if (monthDay >= 823 && monthDay <= 922) return 5;    // 处女座
        if (monthDay >= 923 && monthDay <= 1023) return 6;   // 天秤座
        if (monthDay >= 1024 && monthDay <= 1122) return 7;  // 天蝎座
        if (monthDay >= 1123 && monthDay <= 1221) return 8;  // 射手座
        if (monthDay >= 1222 || monthDay <= 119) return 9;   // 摩羯座
        if (monthDay >= 120 && monthDay <= 218) return 10;   // 水瓶座
        if (monthDay >= 219 && monthDay <= 320) return 11;   // 双鱼座
        return -1;
    }

    /**
     * 校验仅包含月日的生日。由于没有年份，允许 2 月 29 日作为闰年生日。
     */
    private static boolean isValidMonthDay(int month, int day) {
        if (month < 1 || month > 12 || day < 1) return false;
        int[] daysInMonth = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return day <= daysInMonth[month];
    }

    /**
     * 获取星座完整详情
     */
    public static ZodiacInfo getZodiacInfo(int index) {
        if (index < 0 || index > 11) return null;
        ZodiacInfo info = new ZodiacInfo();
        info.index = index;
        info.nameCn = ZODIAC_NAMES_CN[index];
        info.nameEn = ZODIAC_NAMES_EN[index];
        info.unicode = ZODIAC_UNICODE[index];
        info.dateRange = getDateRange(index);
        info.element = ELEMENTS[index];
        info.rulingPlanet = RULING_PLANETS[index];
        info.luckyNumbers = LUCKY_NUMBERS[index];
        info.luckyColors = LUCKY_COLORS[index];
        info.traits = PERSONALITY_TRAITS[index];
        info.description = DESCRIPTIONS[index];
        info.bestMatch = BEST_MATCH[index];
        info.worstMatch = WORST_MATCH[index];
        return info;
    }

    /**
     * 获取日期范围字符串
     */
    private static String getDateRange(int index) {
        int startMonth = ZODIAC_START[index][0];
        int startDay = ZODIAC_START[index][1];

        // 计算结束日期（下一个星座的开始日前一天）
        int endIndex = (index + 1) % 12;
        int endMonth = ZODIAC_START[endIndex][0];
        int endDay = ZODIAC_START[endIndex][1] - 1;

        // 跨年处理
        if (startMonth > endMonth || (startMonth == 12 && endMonth == 1)) {
            return startMonth + "月" + startDay + "日 - " + endMonth + "月" + endDay + "日";
        }

        // 月底处理
        if (endDay == 0) {
            endMonth = endMonth - 1;
            if (endMonth == 0) endMonth = 12;
            if (endMonth == 2) endDay = 28;
            else if (endMonth == 4 || endMonth == 6 || endMonth == 9 || endMonth == 11) endDay = 30;
            else endDay = 31;
        }

        return startMonth + "月" + startDay + "日 - " + endMonth + "月" + endDay + "日";
    }

    /**
     * 获取所有星座列表
     */
    public static List<ZodiacInfo> getAllZodiacSigns() {
        List<ZodiacInfo> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add(getZodiacInfo(i));
        }
        return list;
    }

    /**
     * 为生日（年-月-日格式）返回星座索引
     */
    public static int getZodiacIndexFromBirthday(String birthday) {
        if (birthday == null || birthday.isEmpty()) return -1;
        String[] parts = birthday.split("-");
        if (parts.length < 2) return -1;
        try {
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return getZodiacIndex(month, day);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 生成星座每日运势
     */
    public static Map<String, Object> generateDailyHoroscope(int zodiacIndex, String dateStr) {
        if (zodiacIndex < 0 || zodiacIndex > 11) return null;

        long seed = (long) zodiacIndex * 10000 + dateStr.hashCode();
        Random rand = new Random(seed);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("zodiacIndex", zodiacIndex);
        result.put("zodiacName", ZODIAC_NAMES_CN[zodiacIndex]);
        result.put("unicode", ZODIAC_UNICODE[zodiacIndex]);
        result.put("date", dateStr);

        // 综合评分 (40-99)
        int overallScore = 40 + rand.nextInt(60);
        result.put("overallScore", overallScore);

        // 分项评分
        result.put("careerScore", 30 + rand.nextInt(70));
        result.put("wealthScore", 30 + rand.nextInt(70));
        result.put("loveScore", 30 + rand.nextInt(70));
        result.put("healthScore", 40 + rand.nextInt(60));

        // 运势描述
        result.put("overallDesc", DAILY_FORTUNE_TEMPLATES[0][rand.nextInt(DAILY_FORTUNE_TEMPLATES[0].length)]);
        result.put("careerDesc", DAILY_FORTUNE_TEMPLATES[1][rand.nextInt(DAILY_FORTUNE_TEMPLATES[1].length)]);
        result.put("wealthDesc", DAILY_FORTUNE_TEMPLATES[2][rand.nextInt(DAILY_FORTUNE_TEMPLATES[2].length)]);
        result.put("loveDesc", DAILY_FORTUNE_TEMPLATES[3][rand.nextInt(DAILY_FORTUNE_TEMPLATES[3].length)]);
        result.put("healthDesc", DAILY_FORTUNE_TEMPLATES[4][rand.nextInt(DAILY_FORTUNE_TEMPLATES[4].length)]);

        // 幸运信息
        int[] luckyNums = LUCKY_NUMBERS[zodiacIndex];
        result.put("luckyNumber", luckyNums[rand.nextInt(luckyNums.length)]);
        String[] colors = LUCKY_COLORS[zodiacIndex];
        result.put("luckyColor", colors[rand.nextInt(colors.length)]);
        String[] directions = {"东", "南", "西", "北", "东南", "西南", "东北", "西北"};
        result.put("luckyDirection", directions[rand.nextInt(directions.length)]);

        // 建议
        result.put("advice", ADVICE_TEMPLATES[rand.nextInt(ADVICE_TEMPLATES.length)]);

        return result;
    }

    /**
     * 生成星座周运势
     */
    public static Map<String, Object> generateWeeklyHoroscope(int zodiacIndex, String weekStr) {
        if (zodiacIndex < 0 || zodiacIndex > 11) return null;

        long seed = (long) zodiacIndex * 100000 + weekStr.hashCode();
        Random rand = new Random(seed);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("zodiacIndex", zodiacIndex);
        result.put("zodiacName", ZODIAC_NAMES_CN[zodiacIndex]);
        result.put("unicode", ZODIAC_UNICODE[zodiacIndex]);
        result.put("week", weekStr);

        String[] fortuneStars = {"★★★★★", "★★★★☆", "★★★☆☆", "★★☆☆☆", "★☆☆☆☆"};
        result.put("overallStars", fortuneStars[rand.nextInt(fortuneStars.length)]);
        result.put("description", WEEKLY_TEMPLATES[rand.nextInt(WEEKLY_TEMPLATES.length)]);

        result.put("luckyNumber", 1 + rand.nextInt(99));
        String[] colors = LUCKY_COLORS[zodiacIndex];
        result.put("luckyColor", colors[rand.nextInt(colors.length)]);

        return result;
    }

    /**
     * 生成星座月运势
     */
    public static Map<String, Object> generateMonthlyHoroscope(int zodiacIndex, String monthStr) {
        if (zodiacIndex < 0 || zodiacIndex > 11) return null;

        long seed = (long) zodiacIndex * 1000000 + monthStr.hashCode();
        Random rand = new Random(seed);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("zodiacIndex", zodiacIndex);
        result.put("zodiacName", ZODIAC_NAMES_CN[zodiacIndex]);
        result.put("unicode", ZODIAC_UNICODE[zodiacIndex]);
        result.put("month", monthStr);

        result.put("overallScore", 50 + rand.nextInt(51));
        result.put("description", MONTHLY_TEMPLATES[rand.nextInt(MONTHLY_TEMPLATES.length)]);
        result.put("luckyNumber", 1 + rand.nextInt(99));
        String[] colors = LUCKY_COLORS[zodiacIndex];
        result.put("luckyColor", colors[rand.nextInt(colors.length)]);

        return result;
    }

    /**
     * 星座配对分析
     */
    public static Map<String, Object> getCompatibility(int index1, int index2) {
        if (index1 < 0 || index1 > 11 || index2 < 0 || index2 > 11) return null;

        int score = COMPATIBILITY[index1][index2];
        String level;
        String description;

        if (score >= 90) {
            level = "天生一对";
            description = "你们是天生的一对！彼此吸引，互相理解，在一起能够碰撞出精彩的火花。无论是性格还是价值观都非常契合。";
        } else if (score >= 75) {
            level = "非常相配";
            description = "你们非常相配！彼此欣赏，有共同的兴趣爱好，能够建立稳固而甜蜜的关系。";
        } else if (score >= 60) {
            level = "比较相配";
            description = "你们比较相配，有吸引力但也有需要磨合的地方。只要互相包容理解，关系会更加和谐。";
        } else if (score >= 45) {
            level = "一般相配";
            description = "你们的性格差异较大，需要更多的沟通和理解。如果能尊重彼此的差异，也能建立美好的关系。";
        } else {
            level = "挑战较大";
            description = "你们的组合挑战较大，性格和处事方式差异明显。如果真的相爱，需要付出更多的努力和包容。";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sign1", ZODIAC_NAMES_CN[index1]);
        result.put("sign1Unicode", ZODIAC_UNICODE[index1]);
        result.put("sign2", ZODIAC_NAMES_CN[index2]);
        result.put("sign2Unicode", ZODIAC_UNICODE[index2]);
        result.put("compatibilityScore", score);
        result.put("level", level);
        result.put("description", description);
        result.put("element1", ELEMENTS[index1]);
        result.put("element2", ELEMENTS[index2]);

        // 配对建议
        String[] tips = {
                "多制造浪漫惊喜，保持新鲜感。",
                "学会倾听对方的需求。",
                "给彼此适当的个人空间。",
                "一起规划未来，增进默契。",
                "遇到分歧时保持冷静沟通。",
                "培养共同爱好，增进感情。",
                "互相欣赏和鼓励。",
                "坦诚相待，建立信任。"
        };
        Random rand = new Random((long) index1 * 100 + index2);
        result.put("tip", tips[rand.nextInt(tips.length)]);

        return result;
    }
}
