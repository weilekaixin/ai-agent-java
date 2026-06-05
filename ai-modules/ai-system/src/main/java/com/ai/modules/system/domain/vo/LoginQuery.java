package com.ai.modules.system.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求体
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
public class LoginQuery {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空！")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空！")
    private String password;
}
