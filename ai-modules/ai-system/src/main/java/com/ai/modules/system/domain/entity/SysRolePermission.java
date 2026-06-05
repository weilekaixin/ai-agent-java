package com.ai.modules.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色权限关联
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@TableName("sys_role_permission")
public class SysRolePermission {

    /** 角色ID */
    private Long roleId;

    /** 权限ID */
    private Long permissionId;
}
