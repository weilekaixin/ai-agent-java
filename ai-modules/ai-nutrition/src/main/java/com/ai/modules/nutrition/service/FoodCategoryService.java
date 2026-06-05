package com.ai.modules.nutrition.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ai.common.core.domain.R;
import com.ai.common.redis.utils.RedisUtils;
import com.ai.modules.nutrition.domain.entity.FoodCategory;
import com.ai.modules.nutrition.mapper.FoodCategoryMapper;
import com.ai.modules.nutrition.model.query.FoodCategoryListQuery;
import com.ai.modules.nutrition.model.query.FoodCategoryQuery;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * 食物分类
 *
 * @author zhangyunlong 2026/6/5 00:00
 */
@Slf4j
@Service
public class FoodCategoryService extends ServiceImpl<FoodCategoryMapper, FoodCategory> {

    private static final String CACHE_KEY_LIST = "nutrition:category:list";

    /**
     * 列表分页查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public Page<FoodCategory> listPage(FoodCategoryListQuery query) {
        var wrapper = this.buildWrapper(query).orderByAsc(FoodCategory::getSort);
        return this.page(query.build(), wrapper);
    }

    /**
     * 查询全部分类（Redis 缓存 1 小时，供前端下拉使用）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    public List<FoodCategory> listAll() {
        List<FoodCategory> cached = RedisUtils.getCacheObject(CACHE_KEY_LIST);
        if (CollUtil.isNotEmpty(cached)) {
            return cached;
        }
        List<FoodCategory> list = this.list(Wrappers.lambdaQuery(FoodCategory.class).orderByAsc(FoodCategory::getSort));
        RedisUtils.setCacheObject(CACHE_KEY_LIST, list, Duration.ofHours(1));
        return list;
    }

    /**
     * 新增或更新
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @Transactional(rollbackFor = Exception.class)
    public R<?> addOrUpdate(FoodCategoryQuery query) {
        FoodCategory data;
        if (ObjectUtil.isNotNull(query.getId())) {
            data = baseMapper.selectById(query.getId());
            if (ObjectUtil.isNull(data)) {
                return R.fail("获取分类信息失败，请刷新后重试！");
            }
        } else {
            data = new FoodCategory();
        }
        this.copyFields(data, query);
        this.saveOrUpdate(data);
        this.evictCache();
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
        this.evictCache();
        return R.ok();
    }

    /**
     * 构建查询条件
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private LambdaQueryWrapper<FoodCategory> buildWrapper(FoodCategoryListQuery query) {
        var wrapper = Wrappers.lambdaQuery(FoodCategory.class);
        wrapper.like(StrUtil.isNotBlank(query.getName()), FoodCategory::getName, query.getName())
                .in(CollUtil.isNotEmpty(query.getIdList()), FoodCategory::getId, query.getIdList());
        return wrapper;
    }

    /**
     * 复制字段
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void copyFields(FoodCategory entity, FoodCategoryQuery query) {
        entity.setName(query.getName());
        entity.setIcon(query.getIcon());
        entity.setSort(query.getSort());
    }

    /**
     * 清除分类缓存
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    private void evictCache() {
        RedisUtils.deleteObject(CACHE_KEY_LIST);
    }
}
