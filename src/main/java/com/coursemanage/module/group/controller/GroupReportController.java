package com.coursemanage.module.group.controller;

import com.coursemanage.module.group.entity.GroupReport;
import com.coursemanage.module.group.entity.Groups;
import com.coursemanage.module.group.service.GroupReportService;
import com.coursemanage.module.group.service.GroupsService;
import com.coursemanage.module.group.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupReportController {

    @Autowired
    private GroupsService groupsService; // 小组创建仍保留（若需单独拆分可再建GroupController）

    @Autowired
    private GroupReportService groupReportService;

    // 1. 创建小组（若后续小组功能复杂，可移到单独的GroupController）
    @PostMapping("/create")
    public R<Groups> createGroup(@RequestBody Groups group) {
        Groups result = groupsService.createGroup(group);
        return R.success(result);
    }

    //列出已有小组，根据课程id查询
    @GetMapping("/list")
    public R<List<Groups>> getGroupList(
            @RequestParam Integer courseId // 改为必填，无默认值
    ) {
        // 校验courseId非空（增强健壮性）
        if (courseId == null || courseId <= 0) {
            return R.error(400, "课程ID不能为空且必须为正整数");
        }
        List<Groups> groups = groupsService.getGroupsByCourseId(courseId);
        return R.success(groups);
    }

    // 5. 上传报告
    @PostMapping("/report/upload")
    public R<GroupReport> uploadReport(@RequestBody GroupReport report) {
        GroupReport result = groupReportService.uploadReport(report);
        return R.success(result);
    }

    // 6. 查看报告列表
    @GetMapping("/report/list")
    public R<List<GroupReport>> getReportList() {
        List<GroupReport> list = groupReportService.list();
        return R.success(list);
    }

    // 7. 报告评分
    @PutMapping("/report/score")
    public R<Boolean> scoreReport(@RequestBody GroupReport report) {
        boolean result = groupReportService.scoreReport(report);
        return R.success(result);
    }

    // 8. 查看报告详情
    @GetMapping("/report/{id}")
    public R<GroupReport> getReportDetail(@PathVariable Integer id) {
        GroupReport report = groupReportService.getById(id);
        if (report == null) {
            return R.error(404, "报告不存在");
        }
        return R.success(report);
    }
}