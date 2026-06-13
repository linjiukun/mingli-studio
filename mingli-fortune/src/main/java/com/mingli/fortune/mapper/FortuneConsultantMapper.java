package com.mingli.fortune.mapper;

import com.mingli.fortune.domain.FortuneConsultant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 命理师表 Mapper接口
 */
@Mapper
public interface FortuneConsultantMapper {

    /**
     * 查询命理师列表
     * @param consultant 命理师查询条件
     * @return 命理师列表
     */
    List<FortuneConsultant> selectConsultantList(FortuneConsultant consultant);

    /**
     * 查询已认证命理师列表
     * @return 命理师列表
     */
    List<FortuneConsultant> selectCertifiedConsultants();

    /**
     * 查询命理师详情
     * @param id 命理师ID
     * @return 命理师详情
     */
    FortuneConsultant selectConsultantById(Long id);

    /**
     * 根据用户ID查询命理师
     * @param userId 用户ID
     * @return 命理师信息
     */
    FortuneConsultant selectConsultantByUserId(Long userId);

    /**
     * 新增命理师
     * @param consultant 命理师信息
     * @return 结果
     */
    int insertConsultant(FortuneConsultant consultant);

    /**
     * 修改命理师
     * @param consultant 命理师信息
     * @return 结果
     */
    int updateConsultant(FortuneConsultant consultant);

    /**
     * 删除命理师
     * @param id 命理师ID
     * @return 结果
     */
    int deleteConsultantById(Long id);

    /**
     * 更新命理师评分
     * @param id 命理师ID
     * @param rating 新评分
     * @return 结果
     */
    int updateRating(@Param("id") Long id, @Param("rating") Double rating);

    /**
     * 更新咨询次数
     * @param id 命理师ID
     * @return 结果
     */
    int updateConsultationCount(Long id);
}
