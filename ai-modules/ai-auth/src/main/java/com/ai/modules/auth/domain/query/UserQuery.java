package com.ai.modules.auth.domain.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

/**
 * 新增普通用户
 *
 * @author root
 */
@Data
public class UserQuery {
    /**
     * 用户名长度与数据库字段保持一致
     */
    public static final int USERNAME_MAX_LENGTH = 64;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = USERNAME_MAX_LENGTH, message = "用户名过长")
    private String username;

    /**
     * 初始密码
     */
    @NotBlank(message = "初始密码不能为空")
    @ToString.Exclude
    private String password;
}
