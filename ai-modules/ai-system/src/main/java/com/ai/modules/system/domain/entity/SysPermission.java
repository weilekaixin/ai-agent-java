package com.ai.modules.system.domain.entity;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统权限
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class SysPermission extends BaseEntity {

    /** 权限ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 权限标识（如 system:user:list） */
    private String permKey;

    /** 权限名称 */
    private String permName;

    /** 备注 */
    private String remark;

    /** 软删除（0=未删 1=已删） */
    @TableLogic
    private Integer delFlag;
}
