package com.ai.modules.auth.domain.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 * 管理员重置密码参数
 */
@Data
public class ResetPasswordQuery {
    /**
     * 新初始密码
     */
    @NotBlank(message = "初始密码不能为空")
    @ToString.Exclude
    private String password;
}
