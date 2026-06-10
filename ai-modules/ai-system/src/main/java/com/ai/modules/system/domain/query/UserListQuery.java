package com.ai.modules.system.domain.query;

import com.ai.common.mybatis.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户列表查询
 *
 * @author zhangyunlong
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名（模糊）
     */
    private String account;

    /**
     * 昵称（模糊）
     */
    private String nickname;

    /**
     * 状态（0=正常 1=禁用）
     */
    private Integer status;

    /**
     * 手机号
     */
    private String phone;
}
