package com.ai.modules.auth.domain;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.ai.modules.auth.enums.Permission;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证用户
 *
 * @author root
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "auth_user")
public class User extends BaseEntity implements Serializable {
    /**
     * 用户 ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    /**
     * 登录用户名
     */
    private String username;
    /**
     * 密码摘要，不保存明文密码
     */
    @JsonIgnore
    @ToString.Exclude
    private String password;
    /**
     * 用户角色
     */
    private Permission role;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 是否必须修改初始密码
     */
    private Boolean mustChangePassword;
    @Serial
    private static final long serialVersionUID = 1L;
}
