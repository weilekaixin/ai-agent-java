package com.ai.modules.accounting.domain;

import com.ai.modules.accounting.enums.AccountChangeType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金变动流水，仅追加，不继承包含更新字段的基础实体
 */
@Data
@TableName(value = "accounting_account_flow")
public class AccountFlow implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资金流水 ID
     */
    @TableId(value = "flow_id", type = IdType.ASSIGN_ID)
    private Long flowId;

    /**
     * 所属用户 ID
     */
    private Long userId;

    /**
     * 资金账户 ID
     */
    private Long accountId;

    /**
     * 资金变动类型
     */
    private AccountChangeType changeType;

    /**
     * 变化金额，单位元，增加为正、减少为负
     */
    private BigDecimal changeAmount;

    /**
     * 本次变化后的账户余额，单位元
     */
    private BigDecimal balanceAfter;

    /**
     * 关联业务 ID
     */
    private Long businessId;

    /**
     * 单条资金变动的幂等键，重试时保持不变
     */
    private String requestKey;

    /**
     * 资金变动发生时间
     */
    private Date occurredTime;

    /**
     * 流水说明
     */
    private String details;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
