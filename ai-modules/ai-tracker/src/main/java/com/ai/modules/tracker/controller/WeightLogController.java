package com.ai.modules.tracker.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.tracker.model.query.WeightLogListQuery;
import com.ai.modules.tracker.model.query.WeightLogQuery;
import com.ai.modules.tracker.service.WeightLogService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 体重记录
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 健康追踪/体重记录
 */
@RestController
@RequestMapping("/weight")
public class WeightLogController {

    private static final String MODEL_NAME = "体重记录";

    @Resource
    private WeightLogService weightLogService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<?>> list(@Valid WeightLogListQuery query) {
        return R.ok(weightLogService.listPage(LoginHelper.getUserId(), query));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    @SentinelResource("weight:add")
    public R<?> add(@Valid @RequestBody WeightLogQuery query) {
        return weightLogService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody WeightLogQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return weightLogService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody WeightLogListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return weightLogService.delBatch(query.getIdList());
    }
}
