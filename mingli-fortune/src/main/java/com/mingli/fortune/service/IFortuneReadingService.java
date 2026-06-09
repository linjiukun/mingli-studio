package com.mingli.fortune.service;

import java.util.List;
import java.util.Map;

/**
 * 命理解读服务接口
 */
public interface IFortuneReadingService {

    /**
     * 请求命理解读
     */
    Map<String, Object> requestReading(Long userId, Long chartId, String readingType);

    /**
     * 获取解读历史
     */
    List<Map<String, Object>> getReadings(Long userId);
}
