package com.ai.modules.auth.domain.query;

import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.modules.auth.enums.Permission;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户列表查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListQuery extends PageQuery {
    /**
     * 用户名，模糊匹配
     */
    @Size(max = UserQuery.USERNAME_MAX_LENGTH, message = "用户名过长")
    private String username;

    /**
     * 用户角色，未传时查询全部角色
     */
    private Permission role;

    /**
     * 是否启用，未传时查询全部状态
     */
    private Boolean enabled;
}
