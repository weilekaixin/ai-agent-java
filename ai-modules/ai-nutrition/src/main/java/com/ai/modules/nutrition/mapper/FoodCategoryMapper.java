package com.ai.modules.nutrition.mapper;

import com.ai.modules.nutrition.domain.entity.FoodCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 食物分类 Mapper
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Mapper
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {
}
