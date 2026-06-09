package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.FortuneReading;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 命理解读 Mapper 接口
 */
public interface FortuneReadingMapper {

    FortuneReading selectById(@Param("id") Long id);

    List<FortuneReading> selectByUserId(@Param("userId") Long userId);

    List<FortuneReading> selectList(FortuneReading reading);

    int insert(FortuneReading reading);

    int update(FortuneReading reading);

    int deleteById(@Param("id") Long id);
}
