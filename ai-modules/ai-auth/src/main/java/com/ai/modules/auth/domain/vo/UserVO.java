package com.ai.modules.auth.domain.vo;

import com.ai.modules.auth.enums.Permission;
import lombok.Data;

/**
 * 当前登录用户信息
 *
 * @author root
 */
@Data
public class UserVO {
    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

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
}
