package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.FortuneDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 每日运势 Mapper 接口
 */
public interface FortuneDailyMapper {

    FortuneDaily selectById(@Param("id") Long id);

    FortuneDaily selectByUserAndDate(@Param("userId") Long userId, @Param("fortuneDate") Date fortuneDate);

    List<FortuneDaily> selectByUserAndDateRange(@Param("userId") Long userId,
                                                 @Param("startDate") Date startDate,
                                                 @Param("endDate") Date endDate);

    List<FortuneDaily> selectList(FortuneDaily daily);

    int insert(FortuneDaily daily);

    int update(FortuneDaily daily);

    int deleteById(@Param("id") Long id);
}
