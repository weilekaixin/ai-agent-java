package com.ai.modules.auth.domain.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 登录结果
 *
 * @author root
 */
@Data
public class LoginVO {
    /**
     * Sa-Token 凭据名称
     */
    private String tokenName;
    /**
     * Sa-Token 凭据值
     */
    @ToString.Exclude
    private String tokenValue;
    /**
     * 是否必须修改初始密码
     */
    private Boolean mustChangePassword;
}
