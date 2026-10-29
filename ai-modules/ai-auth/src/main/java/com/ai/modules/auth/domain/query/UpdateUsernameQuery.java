package com.ai.modules.auth.domain.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改用户名参数
 */
@Data
public class UpdateUsernameQuery {
    /**
     * 新用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = UserQuery.USERNAME_MAX_LENGTH, message = "用户名过长")
    private String username;
}
