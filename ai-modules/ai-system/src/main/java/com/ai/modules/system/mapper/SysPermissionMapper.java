package com.ai.modules.system.mapper;

import com.ai.modules.system.domain.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 权限 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询用户的权限标识列表
     *
     * @param userId 用户ID
     */
    @Select("""
            SELECT p.perm_key FROM sys_permission p
            INNER JOIN sys_role_permission rp ON rp.permission_id = p.id
            INNER JOIN sys_user_role ur ON ur.role_id = rp.role_id
            WHERE ur.user_id = #{userId} AND p.del_flag = 0
            """)
    List<String> selectPermKeysByUserId(Long userId);
}
