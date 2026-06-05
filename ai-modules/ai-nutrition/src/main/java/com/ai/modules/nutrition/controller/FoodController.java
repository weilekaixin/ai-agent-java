package com.ai.modules.nutrition.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.nutrition.domain.entity.Food;
import com.ai.modules.nutrition.model.query.FoodListQuery;
import com.ai.modules.nutrition.model.query.FoodQuery;
import com.ai.modules.nutrition.service.FoodService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 食物库
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 营养管理/食物库
 */
@RestController
@RequestMapping("/food")
public class FoodController {

    private static final String MODEL_NAME = "食物";

    @Resource
    private FoodService foodService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    @SentinelResource("food:list")
    public R<Page<Food>> list(@Valid FoodListQuery query) {
        return R.ok(foodService.listPage(query));
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<Food> get(@Valid FoodListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return R.ok(foodService.getById(query.getId()));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody FoodQuery query) {
        return foodService.addOrUpdate(query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody FoodQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return foodService.addOrUpdate(query);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody FoodListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return foodService.delBatch(query.getIdList());
    }
}
