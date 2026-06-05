package com.ai.modules.system.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.ai.modules.system.mapper.SysPermissionMapper;
import com.ai.modules.system.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token 权限数据源
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionMapper sysPermissionMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return sysPermissionMapper.selectPermKeysByUserId(parseUserId(loginId));
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return sysRoleMapper.selectRoleKeysByUserId(parseUserId(loginId));
    }

    private Long parseUserId(Object loginId) {
        // loginId 格式为 "userType:userId"，取冒号后的 userId
        String id = loginId.toString();
        int idx = id.lastIndexOf(':');
        return Long.parseLong(idx >= 0 ? id.substring(idx + 1) : id);
    }
}
