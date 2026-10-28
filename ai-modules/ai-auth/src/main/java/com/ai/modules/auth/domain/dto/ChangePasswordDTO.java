package com.ai.modules.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 * 修改本人密码
 *
 * @author root
 */
@Data
public class ChangePasswordDTO {
    /**
     * 原密码
     */
    @NotBlank(message = "原密码不能为空")
    @ToString.Exclude
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @ToString.Exclude
    private String newPassword;
}
