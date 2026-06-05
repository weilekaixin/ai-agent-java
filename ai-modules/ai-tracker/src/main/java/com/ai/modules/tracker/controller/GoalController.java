package com.ai.modules.tracker.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.ai.common.core.domain.R;
import com.ai.common.satoken.utils.LoginHelper;
import com.ai.modules.tracker.model.query.GoalListQuery;
import com.ai.modules.tracker.model.query.GoalQuery;
import com.ai.modules.tracker.service.GoalService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 目标管理
 * 控制层
 *
 * @author zhangyunlong 2026/6/5 00:00
 * @folder 健康追踪/目标管理
 */
@RestController
@RequestMapping("/goal")
public class GoalController {

    private static final String MODEL_NAME = "目标";

    @Resource
    private GoalService goalService;

    /**
     * 列表查询
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @GetMapping("/list")
    public R<Page<?>> list(@Valid GoalListQuery query) {
        return R.ok(goalService.listPage(LoginHelper.getUserId(), query));
    }

    /**
     * 新增
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/add")
    public R<?> add(@Valid @RequestBody GoalQuery query) {
        return goalService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 编辑
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/update")
    public R<?> update(@RequestBody GoalQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return goalService.addOrUpdate(LoginHelper.getUserId(), query);
    }

    /**
     * 标记达成
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/achieve")
    public R<?> achieve(@RequestBody GoalListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return goalService.changeStatus(query.getId(), LoginHelper.getUserId(), "achieved");
    }

    /**
     * 放弃目标
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/abandon")
    public R<?> abandon(@RequestBody GoalListQuery query) {
        if (ObjUtil.isNull(query.getId())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        return goalService.changeStatus(query.getId(), LoginHelper.getUserId(), "abandoned");
    }

    /**
     * 批量删除
     *
     * @author zhangyunlong 2026/6/5 00:00
     */
    @PostMapping("/del_batch")
    public R<?> delBatch(@RequestBody GoalListQuery query) {
        if (CollUtil.isEmpty(query.getIdList())) {
            return R.fail("请选择" + MODEL_NAME + "数据！");
        }
        query.setIdList(query.getIdList().stream().distinct().toList());
        return goalService.delBatch(query.getIdList());
    }
}
