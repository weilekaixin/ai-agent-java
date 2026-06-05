package com.ai.modules.nutrition.service;

import com.ai.common.core.exception.BusinessException;
import com.ai.common.mybatis.core.page.PageQuery;
import com.ai.common.redis.utils.RedisUtils;
import com.ai.modules.nutrition.domain.entity.Food;
import com.ai.modules.nutrition.mapper.FoodMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 食物库业务
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class FoodService {

    private static final String CACHE_PREFIX = "nutrition:food:";

    private final FoodMapper foodMapper;

    /** 分页查询食物列表 */
    public IPage<Food> page(String name, Long categoryId, PageQuery pageQuery) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<Food>()
                .like(StrUtil.isNotBlank(name), Food::getName, name)
                .eq(ObjectUtil.isNotNull(categoryId), Food::getCategoryId, categoryId)
                .orderByDesc(Food::getCreateTime);
        return foodMapper.selectPage(pageQuery.build(), wrapper);
    }

    /** 查单个食物（先走缓存） */
    public Food getById(Long id) {
        String key = CACHE_PREFIX + id;
        Food cached = RedisUtils.getCacheObject(key);
        if (cached != null) {
            return cached;
        }
        Food food = foodMapper.selectById(id);
        if (food != null) {
            RedisUtils.setCacheObject(key, food, Duration.ofHours(6));
        }
        return food;
    }

    /** 新增食物 */
    public void add(Food food) {
        foodMapper.insert(food);
    }

    /** 修改食物，删除缓存 */
    public void update(Food food) {
        if (ObjectUtil.isNull(food.getId())) {
            throw new BusinessException("食物ID不能为空！");
        }
        foodMapper.updateById(food);
        RedisUtils.deleteObject(CACHE_PREFIX + food.getId());
    }

    /** 删除食物，删除缓存 */
    public void remove(Long id) {
        foodMapper.deleteById(id);
        RedisUtils.deleteObject(CACHE_PREFIX + id);
    }
}
