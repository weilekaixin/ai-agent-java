package com.ai.common.mybatis.core.page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.exception.BusinessException;
import com.ai.common.core.utils.StringUtils;
import com.ai.common.core.utils.sql.SqlUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 分页查询实体类
 *
 * @author root 2026-05-16 16:04
 */
@Data
@NoArgsConstructor
public class PageQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 默认页号
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认每页条数
     */
    public static final int DEFAULT_PAGE_SIZE = 20;
    /**
     * 每页最大条数
     */
    public static final int MAX_PAGE_SIZE = 100;
    /**
     * 排序列只允许字段名或带表别名的字段名
     */
    private static final String SORT_COLUMN_PATTERN = "[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)?";

    /**
     * 分页大小
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 当前页数
     */
    private Integer pageNum = DEFAULT_PAGE_NUM;
    /**
     * 排序列，支持逗号分隔
     */
    private String orderByColumn;
    /**
     * 排序方向，支持 asc、desc 及前端对应形式
     */
    private String isAsc = "asc";

    /**
     * 设置页号，空值和非正数使用默认值
     */
    public void setPageNum(Integer pageNum) {
        this.pageNum = ObjUtil.defaultIfNull(pageNum, DEFAULT_PAGE_NUM);
        if (this.pageNum < DEFAULT_PAGE_NUM) {
            this.pageNum = DEFAULT_PAGE_NUM;
        }
    }

    /**
     * 设置分页条数，空值和非正数使用默认值，超限使用上限
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = ObjUtil.defaultIfNull(pageSize, DEFAULT_PAGE_SIZE);
        if (this.pageSize <= 0) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = Math.min(this.pageSize, MAX_PAGE_SIZE);
    }

    /**
     * 排序列统一转为数据库字段命名
     */
    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = StrUtil.isBlankOrUndefined(orderByColumn)
            ? null : StringUtils.toUnderScoreCase(orderByColumn.trim());
    }

    /**
     * 排序方向统一大小写，空值保留默认方向
     */
    public void setIsAsc(String isAsc) {
        if (StrUtil.isBlankOrUndefined(isAsc)) {
            return;
        }
        this.isAsc = isAsc.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * 构建分页和排序对象
     */
    public <T> Page<T> build() {
        this.setPageNum(pageNum);
        this.setPageSize(pageSize);
        Page<T> page = new Page<>(pageNum, pageSize);
        List<OrderItem> orderItems = this.buildOrderItem();
        if (CollUtil.isNotEmpty(orderItems)) {
            page.addOrder(orderItems);
        }
        return page;
    }

    /**
     * 构建单列或多列排序，禁止将 SQL 片段作为排序列传入
     */
    private List<OrderItem> buildOrderItem() {
        if (StrUtil.isBlank(orderByColumn)) {
            return null;
        }
        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        String[] columnList = orderBy.split(StringUtils.SEPARATOR, -1);
        String[] directionList = isAsc.split(StringUtils.SEPARATOR, -1);
        if (directionList.length != 1 && directionList.length != columnList.length) {
            throw new BusinessException("排序参数有误");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (int index = 0; index < columnList.length; index++) {
            String column = columnList[index].trim();
            if (!column.matches(SORT_COLUMN_PATTERN)) {
                throw new BusinessException("排序列格式不正确");
            }
            String direction = directionList.length == 1 ? directionList[0].trim() : directionList[index].trim();
            if ("asc".equals(direction) || "ascending".equals(direction)) {
                orderItems.add(OrderItem.asc(column));
            } else if ("desc".equals(direction) || "descending".equals(direction)) {
                orderItems.add(OrderItem.desc(column));
            } else {
                throw new BusinessException("排序参数有误");
            }
        }
        return orderItems;
    }

    @JsonIgnore
    public Integer getFirstNum() {
        return (pageNum - DEFAULT_PAGE_NUM) * pageSize;
    }

    public PageQuery(Integer pageSize, Integer pageNum) {
        this.setPageSize(pageSize);
        this.setPageNum(pageNum);
    }
}
