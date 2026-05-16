package com.ai.common.core.utils.sql;

import com.ai.common.core.exception.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * sql操作工具类
 *
 * @author root
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtil {

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static final String SQL_PATTERN = "[a-zA-Z0-9_ .\\-,]+";

    /**
     * 检查字符串，防止注入
     *
     * @param value 需要检查的字符串
     */
    public static void filterKeyword(String value) {
        if (!value.matches(SQL_PATTERN)) {
            throw new BusinessException("参数中包含非法字符");
        }
    }

    /**
     * 转义排序参数，防止SQL注入
     *
     * @param orderByColumn 排序列名
     * @return 转义后的列名
     */
    public static String escapeOrderBySql(String orderByColumn) {
        if (orderByColumn == null || orderByColumn.isEmpty()) {
            return "";
        }
        // 去除首尾空格
        orderByColumn = orderByColumn.trim();
        // 过滤非法字符
        filterKeyword(orderByColumn);
        return orderByColumn;
    }
}
