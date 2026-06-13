package com.mingli.fortune.controller;

import com.mingli.common.core.Result;
import com.mingli.fortune.domain.FortuneConsultant;
import com.mingli.fortune.service.IFortuneConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 命理师 Controller
 */
@RestController
@RequestMapping("/api/consultants")
public class FortuneConsultantController {
    
    @Autowired
    private IFortuneConsultantService consultantService;
    
    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("login_user");
    }
    
    /**
     * 获取已认证命理师列表
     */
    @GetMapping
    public Result getConsultants() {
        List<FortuneConsultant> consultants = consultantService.selectCertifiedConsultants();
        return Result.success(consultants);
    }
    
    /**
     * 获取命理师详情
     */
    @GetMapping("/{id}")
    public Result getConsultant(@PathVariable Long id) {
        FortuneConsultant consultant = consultantService.selectConsultantById(id);
        if (consultant == null) {
            return Result.error("命理师不存在");
        }
        return Result.success(consultant);
    }
    
    /**
     * 申请成为命理师
     */
    @PostMapping("/apply")
    public Result applyConsultant(@RequestBody FortuneConsultant consultant, HttpServletRequest request) {
        Long userId = getUserId(request);
        
        // 检查是否已申请
        FortuneConsultant existing = consultantService.selectConsultantByUserId(userId);
        if (existing != null) {
            return Result.error("您已申请过，请勿重复申请");
        }
        
        consultant.setUserId(userId);
        consultant.setStatus("0"); // 待审核
        consultant.setRating(5.0);
        consultant.setConsultationCount(0);
        consultant.setCreateTime(new Date());
        consultant.setUpdateTime(new Date());
        
        int result = consultantService.insertConsultant(consultant);
        if (result > 0) {
            return Result.success("申请成功，等待审核", consultant);
        }
        return Result.error("申请失败");
    }
    
    /**
     * 更新命理师信息
     */
    @PutMapping("/{id}")
    public Result updateConsultant(@PathVariable Long id, @RequestBody FortuneConsultant consultant,
                                  HttpServletRequest request) {
        Long userId = getUserId(request);
        FortuneConsultant existing = consultantService.selectConsultantById(id);
        
        if (existing == null) {
            return Result.error("命理师不存在");
        }
        if (!existing.getUserId().equals(userId)) {
            return Result.error("只能修改自己的信息");
        }
        
        consultant.setId(id);
        consultant.setUpdateTime(new Date());
        int result = consultantService.updateConsultant(consultant);
        if (result > 0) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
    
    /**
     * 获取当前用户的命理师信息
     */
    @GetMapping("/my")
    public Result getMyConsultant(HttpServletRequest request) {
        Long userId = getUserId(request);
        FortuneConsultant consultant = consultantService.selectConsultantByUserId(userId);
        if (consultant == null) {
            return Result.error("您还不是命理师");
        }
        return Result.success(consultant);
    }
}