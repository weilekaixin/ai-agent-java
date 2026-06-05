package com.ai.modules.system.mapper;

import com.ai.modules.system.domain.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
