package com.ai.modules.system.mapper;

import com.ai.modules.system.domain.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询用户的角色标识列表
     *
     * @param userId 用户ID
     */
    @Select("""
            SELECT r.role_key FROM sys_role r
            INNER JOIN sys_user_role ur ON ur.role_id = r.id
            WHERE ur.user_id = #{userId} AND r.del_flag = 0 AND r.status = 0
            """)
    List<String> selectRoleKeysByUserId(Long userId);
}
