package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.FortuneDivination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 占卜记录 Mapper 接口
 */
public interface FortuneDivinationMapper {

    FortuneDivination selectById(@Param("id") Long id);

    List<FortuneDivination> selectByUserId(@Param("userId") Long userId);

    List<FortuneDivination> selectList(FortuneDivination divination);

    int insert(FortuneDivination divination);

    int update(FortuneDivination divination);

    int deleteById(@Param("id") Long id);
}
