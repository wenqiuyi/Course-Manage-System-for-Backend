package com.coursemanage.module.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanage.module.group.entity.GroupReport;

public interface GroupReportService extends IService<GroupReport> {
    GroupReport uploadReport(GroupReport report); // 上传报告
    boolean scoreReport(GroupReport report); // 评分
}