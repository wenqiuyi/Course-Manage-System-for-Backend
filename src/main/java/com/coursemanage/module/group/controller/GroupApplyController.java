package com.coursemanage.module.group.controller;

import com.coursemanage.module.group.entity.GroupApply;
import com.coursemanage.module.group.service.impl.GroupApplyServiceImpl;
import com.coursemanage.module.group.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupApplyController {

    @Autowired
    private GroupApplyServiceImpl groupApplyService;

    // 1. 学生申请加入小组
    @PostMapping("/join") // 路径：/api/group/join
    public R<GroupApply> applyJoinGroup(@RequestBody GroupApply apply) {
        // 调用Service层处理申请逻辑
        GroupApply result = groupApplyService.applyJoinGroup(apply);
        return R.success(result);
    }

    // 2.“查看申请列表”接口修改为：
    @GetMapping("/apply/list")
    public R<List<GroupApply>> getApplyList() {
        List<GroupApply> list = groupApplyService.getApplyList();
        return R.success(list);
    }

    // 3. 老师审批申请
    @PutMapping("/apply/approve")
    public R<Boolean> approveApply(@RequestBody GroupApply apply) {
        boolean result = groupApplyService.approveApply(apply);
        return R.success(result);
    }
}