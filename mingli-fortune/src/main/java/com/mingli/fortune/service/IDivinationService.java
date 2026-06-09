package com.mingli.fortune.service;

import java.util.List;
import java.util.Map;

/**
 * 占卜服务接口
 */
public interface IDivinationService {

    /**
     * 问卦
     */
    Map<String, Object> askDivination(Long userId, String question, String method);
}
