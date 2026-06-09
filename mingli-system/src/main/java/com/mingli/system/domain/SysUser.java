package com.mingli.system.domain;

import com.mingli.common.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户表 sys_user 实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String realName;
    private String gender;
    private Date birthDate;
    private String birthHour;
    private String birthPlace;
    private String email;
    private String avatar;
    private String status;
}
