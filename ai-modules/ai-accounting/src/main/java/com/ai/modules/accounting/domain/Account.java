package com.ai.modules.accounting.domain;

import com.ai.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 用户资金账户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "accounting_account")
public class Account extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资金账户 ID
     */
    @TableId(value = "account_id", type = IdType.ASSIGN_ID)
    private Long accountId;

    /**
     * 所属用户 ID
     */
    private Long userId;

    /**
     * 账户名称
     */
    private String name;

    /**
     * 账面余额，单位元，包含提前入账的返现
     */
    private BigDecimal balance;

    /**
     * 显示顺序，升序排列
     */
    private Integer sortOrder;

    /**
     * 是否隐藏，隐藏后仍计入总余额
     */
    private Boolean hidden;
}
