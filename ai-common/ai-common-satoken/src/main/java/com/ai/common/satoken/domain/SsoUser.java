package com.ai.common.satoken.domain;

import com.ai.common.core.enums.UserRoleType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录用户信息（主会话级别）
 *
 * @author zhangyunlong
 */
@Data
public class SsoUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户账号 */
    private String account;

    /** 用户昵称 */
    private String nickname;

    /** 手机号 */
    private String phone;

    /** 用户类型 */
    private String userType;

    /** 用户角色类型 1-超级管理员，2-子管理员，3-普通用户 */
    private Integer userRoleType;

    /** 是否初始密码 0-不是 1-是 */
    private Integer initPassword;

    /** 是否为超级管理员 */
    public boolean isSuperAdmin() {
        return UserRoleType.isSuperAdmin(userRoleType);
    }

    /** 是否为管理员（含子管理员） */
    public boolean isAdmin() {
        return UserRoleType.isAdmin(userRoleType);
    }
}
