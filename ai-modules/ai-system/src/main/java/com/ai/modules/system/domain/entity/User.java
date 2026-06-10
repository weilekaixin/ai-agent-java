package com.ai.modules.system.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统用户
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    /**
     * 用户ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户名
     */
    private String account;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * 密码盐值
     */
    @JsonIgnore
    private String salt;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户角色类型（1=超级管理员 2=子管理员 3=普通用户）
     */
    private Integer userRoleType;
    /**
     * 状态（0=正常 1=禁用）
     */
    private Integer status;
    /**
     * 是否初始密码（0=否 1=是）
     */
    private Integer initPassword;
    /**
     * 密码最后修改时间
     */
    private Date pwdUpdateDate;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 软删除（0=未删 1=已删）
     */
    @TableLogic
    private Integer delFlag;
}
