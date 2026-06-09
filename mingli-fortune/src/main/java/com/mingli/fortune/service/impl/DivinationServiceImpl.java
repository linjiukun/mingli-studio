package com.mingli.fortune.service.impl;

import cn.hutool.core.date.DateUtil;
import com.mingli.fortune.domain.FortuneDivination;
import com.mingli.fortune.mapper.FortuneDivinationMapper;
import com.mingli.fortune.service.IDivinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DivinationServiceImpl implements IDivinationService {

    @Autowired
    private FortuneDivinationMapper divinationMapper;

    // 八卦名称
    private static final String[] HEXAGRAM_NAMES = {
            "乾为天", "坤为地", "水雷屯", "山水蒙", "水天需", "天水讼", "地水师", "水地比",
            "风天小畜", "天泽履", "地天泰", "天地否", "天火同人", "火天大有", "地山谦", "雷地豫",
            "泽雷随", "山风蛊", "地泽临", "风地观", "火雷噬嗑", "山火贲", "山地剥", "地雷复",
            "天雷无妄", "山天大畜", "山雷颐", "泽风大过", "坎为水", "离为火",
            "泽山咸", "雷风恒", "天山遁", "雷天大壮", "火地晋", "地火明夷",
            "风火家人", "火泽睽", "水山蹇", "雷水解", "山泽损", "风雷益",
            "泽天夬", "天风姤", "泽地萃", "地风升", "泽水困", "水风井",
            "泽火革", "火风鼎", "震为雷", "艮为山", "风山渐", "雷泽归妹",
            "雷火丰", "火山旅", "巽为风", "兑为泽", "风水涣", "水泽节",
            "风泽中孚", "雷山小过", "水火既济", "火水未济"
    };

    private static final Map<String, String[]> HEXAGRAM_ADVICE = new LinkedHashMap<>();

    static {
        HEXAGRAM_ADVICE.put("乾为天", new String[]{"䷀", "天行健，君子以自强不息。运势强盛，宜积极进取。"});
        HEXAGRAM_ADVICE.put("坤为地", new String[]{"䷁", "地势坤，君子以厚德载物。宜包容沉稳，顺势而为。"});
        HEXAGRAM_ADVICE.put("水雷屯", new String[]{"䷂", "万事开头难，宜坚守初心，循序渐进。"});
        HEXAGRAM_ADVICE.put("山水蒙", new String[]{"䷃", "蒙昧初开，宜虚心学习，不宜冒进。"});
        HEXAGRAM_ADVICE.put("水天需", new String[]{"䷄", "需待时机，不宜急躁，静候佳音。"});
        HEXAGRAM_ADVICE.put("天水讼", new String[]{"䷅", "争讼之象，宜退让求和，不宜争锋相对。"});
        HEXAGRAM_ADVICE.put("地水师", new String[]{"䷆", "师出有名，但需谨慎行事，团结力量。"});
        HEXAGRAM_ADVICE.put("水地比", new String[]{"䷇", "亲比和谐，宜广结善缘，合作共赢。"});
        HEXAGRAM_ADVICE.put("地天泰", new String[]{"䷊", "天地交泰，万事通达，宜把握良机。"});
        HEXAGRAM_ADVICE.put("天地否", new String[]{"䷋", "闭塞不通，宜静待时机，修身养性。"});
        HEXAGRAM_ADVICE.put("天火同人", new String[]{"䷌", "志同道合，宜团结协作，共谋发展。"});
        HEXAGRAM_ADVICE.put("火天大有", new String[]{"䷍", "大有收获，宜知足感恩，回馈他人。"});
        HEXAGRAM_ADVICE.put("地山谦", new String[]{"䷎", "谦逊受益，宜低调行事，虚怀若谷。"});
        HEXAGRAM_ADVICE.put("雷地豫", new String[]{"䷏", "愉悦安乐，宜顺势而为，享受当下。"});
        HEXAGRAM_ADVICE.put("泽雷随", new String[]{"䷐", "随顺自然，宜灵活应变，不固执己见。"});
        HEXAGRAM_ADVICE.put("山风蛊", new String[]{"䷑", "整顿治理，宜革新除弊，拨乱反正。"});
        HEXAGRAM_ADVICE.put("坎为水", new String[]{"䷜", "险阻重重，宜谨慎行事，坚守信念。"});
        HEXAGRAM_ADVICE.put("离为火", new String[]{"䷝", "光明照耀，宜发挥才华，积极进取。"});
    }

    @Override
    public Map<String, Object> askDivination(Long userId, String question, String method) {
        // 基于问题和时间生成随机卦象
        int seed = (userId.toString() + question + DateUtil.today()).hashCode();
        Random rand = new Random(seed);

        // 从预设卦象中选取
        List<Map.Entry<String, String[]>> entries = new ArrayList<>(HEXAGRAM_ADVICE.entrySet());
        Map.Entry<String, String[]> selected;
        if (rand.nextBoolean() && false) {
            // 使用预设的64卦
            int idx = Math.abs(rand.nextInt(HEXAGRAM_NAMES.length));
            String name = HEXAGRAM_NAMES[idx];
            // 生成卦符 (用二进制表示上下卦)
            String symbol = String.format("%06d", rand.nextInt(1000000));
            selected = new AbstractMap.SimpleEntry<>(name, new String[]{symbol, "此卦变幻莫测，需结合具体问题解读。"});
        } else {
            selected = entries.get(Math.abs(rand.nextInt(entries.size())));
        }

        // 构建解读文本
        String interpretation = String.format(
                "您占问：「%s」\n【卦象】%s\n【卦辞】%s\n\n此卦象显示，您所问之事%s。",
                question,
                selected.getValue()[0],
                selected.getValue()[1],
                rand.nextBoolean() ? "目前处于变动之中，宜静观其变" : "已有端倪，可顺势而为"
        );

        String advice = selected.getValue()[1];

        // 保存记录
        FortuneDivination div = new FortuneDivination();
        div.setUserId(userId);
        div.setQuestion(question);
        div.setDivinationMethod(method);
        div.setHexagramName(selected.getKey());
        div.setHexagramSymbol(selected.getValue()[0]);
        div.setInterpretation(interpretation);
        div.setAdvice(advice);
        div.setCreateTime(new Date());
        divinationMapper.insert(div);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", div.getId());
        result.put("hexagramName", selected.getKey());
        result.put("hexagramSymbol", selected.getValue()[0]);
        result.put("interpretation", interpretation);
        result.put("advice", advice);
        return result;
    }
}
