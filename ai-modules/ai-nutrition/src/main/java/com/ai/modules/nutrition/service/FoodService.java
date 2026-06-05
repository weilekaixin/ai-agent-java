package com.ai.modules.nutrition.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.domain.R;
import com.ai.common.redis.utils.RedisUtils;
import com.ai.modules.nutrition.domain.entity.Food;
import com.ai.modules.nutrition.mapper.FoodMapper;
import com.ai.modules.nutrition.model.query.FoodListQuery;
import com.ai.modules.nutrition.model.query.FoodQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

/**
 * 食物库
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class FoodService extends ServiceImpl<FoodMapper, Food> {

    private static final String CACHE_PREFIX = "nutrition:food:";

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<Food> listPage(FoodListQuery query) {
        var wrapper = this.buildWrapper(query).orderByDesc(Food::getCreateTime);
        var page = this.page(query.build(), wrapper);
        page.getRecords().forEach(this::buildData);
        return page;
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Food getById(Long id) {
        if (ObjectUtil.isNull(id)) {
            return null;
        }
        String key = CACHE_PREFIX + id;
        Food cached = RedisUtils.getCacheObject(key);
        if (ObjectUtil.isNotNull(cached)) {
            return cached;
        }
        var data = baseMapper.selectById(id);
        this.buildData(data);
        if (ObjectUtil.isNotNull(data)) {
            RedisUtils.setCacheObject(key, data, Duration.ofHours(6));
        }
        return data;
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(FoodQuery query) {
        Food data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取食物信息失败，请刷新后重试！");
            }
            RedisUtils.deleteObject(CACHE_PREFIX + query.getId());
        } else {
            data = new Food();
            data.setSource("manual");
        }
        this.copyFields(data, query);
        this.saveOrUpdate(data);
        return R.ok();
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> delBatch(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return R.ok();
        }
        this.removeByIds(idList);
        idList.forEach(id -> RedisUtils.deleteObject(CACHE_PREFIX + id));
        return R.ok();
    }

    /**
     * 数据构建
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void buildData(Food data) {
        if (ObjectUtil.isNull(data)) {
            return;
        }
        // 预留：后续可在此填充分类名称、单位翻译等
    }

    /**
     * 构建查询条件
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private LambdaQueryWrapper<Food> buildWrapper(FoodListQuery query) {
        var wrapper = Wrappers.lambdaQuery(Food.class);
        wrapper.like(StrUtil.isNotBlank(query.getName()), Food::getName, query.getName())
                .eq(ObjectUtil.isNotNull(query.getCategoryId()), Food::getCategoryId, query.getCategoryId())
                .in(CollUtil.isNotEmpty(query.getIdList()), Food::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(Food entity, FoodQuery query) {
        entity.setName(query.getName());
        entity.setCategoryId(query.getCategoryId());
        entity.setCalories(ObjectUtil.defaultIfNull(query.getCalories(), BigDecimal.ZERO));
        entity.setProtein(ObjectUtil.defaultIfNull(query.getProtein(), BigDecimal.ZERO));
        entity.setFat(ObjectUtil.defaultIfNull(query.getFat(), BigDecimal.ZERO));
        entity.setCarbs(ObjectUtil.defaultIfNull(query.getCarbs(), BigDecimal.ZERO));
        entity.setFiber(ObjectUtil.defaultIfNull(query.getFiber(), BigDecimal.ZERO));
        entity.setUnit(StrUtil.blankToDefault(query.getUnit(), "g"));
    }
}
