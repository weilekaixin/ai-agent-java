package com.ai.modules.nutrition.service;

import com.ai.modules.nutrition.domain.entity.FoodCategory;
import com.ai.common.redis.utils.RedisUtils;
import com.ai.modules.nutrition.mapper.FoodCategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 食物分类业务
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Service
@RequiredArgsConstructor
public class FoodCategoryService {

    private static final String CACHE_KEY = "nutrition:category:list";

    private final FoodCategoryMapper foodCategoryMapper;

    /** 查全部分类（Redis 缓存 1 小时） */
    public List<FoodCategory> listAll() {
        List<FoodCategory> cached = RedisUtils.getCacheObject(CACHE_KEY);
        if (cached != null) {
            return cached;
        }
        List<FoodCategory> list = foodCategoryMapper.selectList(
                new LambdaQueryWrapper<FoodCategory>().orderByAsc(FoodCategory::getSort));
        RedisUtils.setCacheObject(CACHE_KEY, list, Duration.ofHours(1));
        return list;
    }

    /** 新增分类，清除缓存 */
    public void add(FoodCategory category) {
        foodCategoryMapper.insert(category);
        RedisUtils.deleteObject(CACHE_KEY);
    }

    /** 修改分类，清除缓存 */
    public void update(FoodCategory category) {
        foodCategoryMapper.updateById(category);
        RedisUtils.deleteObject(CACHE_KEY);
    }

    /** 删除分类，清除缓存 */
    public void remove(Long id) {
        foodCategoryMapper.deleteById(id);
        RedisUtils.deleteObject(CACHE_KEY);
    }
}
