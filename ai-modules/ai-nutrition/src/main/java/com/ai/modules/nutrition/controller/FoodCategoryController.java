package com.ai.modules.nutrition.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.modules.nutrition.domain.entity.FoodCategory;
import com.ai.modules.nutrition.model.query.FoodCategoryListQuery;
import com.ai.modules.nutrition.model.query.FoodCategoryQuery;
import com.ai.modules.nutrition.service.FoodCategoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 食物分类
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 营养管理/食物分类
 */
@RestController
@RequestMapping("/category")
public class FoodCategoryController {

    private static final String MODEL_NAME = "食物分类";

    @Resource
    private FoodCategoryService foodCategoryService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<FoodCategory>> list(@Valid FoodCategoryListQuery query) {
        return R.ok(foodCategoryService.listPage(query));
    }

    /**
     * 全部分类（下拉使用）
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list_all")
    public R<List<FoodCategory>> listAll() {
        return R.ok(foodCategoryService.listAll());
    }

    /**
     * 详情
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/get")
    public R<FoodCategory> get(@Valid FoodCategoryListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return R.ok(foodCategoryService.getById(query.getId()));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody FoodCategoryQuery query) {
        return foodCategoryService.addOrUpdate(query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody FoodCategoryQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return foodCategoryService.addOrUpdate(query);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody FoodCategoryListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return foodCategoryService.delBatch(query.getIdList());
    }
}
