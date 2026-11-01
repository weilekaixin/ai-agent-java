package com.ai.modules.accounting.domain.query;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 新增资金账户参数
 */
@Data
public class AccountQuery {
    /**
     * 账户名称最大长度，与数据库字段保持一致
     */
    public static final int NAME_MAX_LENGTH = 64;

    /**
     * 金额最大整数位数
     */
    public static final int AMOUNT_INTEGER_DIGITS = 12;

    /**
     * 金额小数位数
     */
    public static final int AMOUNT_SCALE = 2;

    /**
     * 新账户默认显示顺序
     */
    public static final int DEFAULT_SORT_ORDER = 0;

    /**
     * 账户名称
     */
    @NotBlank(message = "账户名称不能为空")
    @Size(max = NAME_MAX_LENGTH, message = "账户名称过长")
    private String name;

    /**
     * 初始余额，单位元，未传时为零
     */
    @NotNull(message = "初始余额不能为空")
    @DecimalMin(value = "0", message = "初始余额不能为负数")
    @Digits(integer = AMOUNT_INTEGER_DIGITS, fraction = AMOUNT_SCALE, message = "余额最多十二位整数、两位小数")
    private BigDecimal openingBalance = BigDecimal.ZERO;
}
