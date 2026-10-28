package com.ai.modules.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 * 登录请求
 *
 * @author root
 */
@Data
public class LoginDTO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ToString.Exclude
    private String password;
}
