package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.FortuneBirthChart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 八字命盘 Mapper 接口
 */
public interface FortuneBirthChartMapper {

    FortuneBirthChart selectById(@Param("id") Long id);

    List<FortuneBirthChart> selectByUserId(@Param("userId") Long userId);

    List<FortuneBirthChart> selectList(FortuneBirthChart chart);

    int insert(FortuneBirthChart chart);

    int update(FortuneBirthChart chart);

    int deleteById(@Param("id") Long id);
}
