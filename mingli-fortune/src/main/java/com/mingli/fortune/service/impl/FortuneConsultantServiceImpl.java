package com.mingli.fortune.service.impl;

import com.mingli.fortune.domain.FortuneConsultant;
import com.mingli.fortune.mapper.FortuneConsultantMapper;
import com.mingli.fortune.service.IFortuneConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 命理师表 Service实现
 */
@Service
public class FortuneConsultantServiceImpl implements IFortuneConsultantService {
    
    @Autowired
    private FortuneConsultantMapper consultantMapper;
    
    @Override
    public List<FortuneConsultant> selectConsultantList(FortuneConsultant consultant) {
        return consultantMapper.selectConsultantList(consultant);
    }
    
    @Override
    public List<FortuneConsultant> selectCertifiedConsultants() {
        return consultantMapper.selectCertifiedConsultants();
    }
    
    @Override
    public FortuneConsultant selectConsultantById(Long id) {
        return consultantMapper.selectConsultantById(id);
    }
    
    @Override
    public FortuneConsultant selectConsultantByUserId(Long userId) {
        return consultantMapper.selectConsultantByUserId(userId);
    }
    
    @Override
    public int insertConsultant(FortuneConsultant consultant) {
        return consultantMapper.insertConsultant(consultant);
    }
    
    @Override
    public int updateConsultant(FortuneConsultant consultant) {
        return consultantMapper.updateConsultant(consultant);
    }
    
    @Override
    public int deleteConsultantById(Long id) {
        return consultantMapper.deleteConsultantById(id);
    }
    
    @Override
    public int updateRating(Long id, Double rating) {
        return consultantMapper.updateRating(id, rating);
    }
    
    @Override
    public int updateConsultationCount(Long id) {
        return consultantMapper.updateConsultationCount(id);
    }
}