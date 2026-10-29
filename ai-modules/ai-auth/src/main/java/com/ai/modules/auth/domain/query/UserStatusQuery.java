package com.ai.modules.auth.domain.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户启用状态
 *
 * @author root
 */
@Data
public class UserStatusQuery {
    /**
     * 是否启用
     */
    @NotNull(message = "启用状态不能为空")
    private Boolean enabled;
}
