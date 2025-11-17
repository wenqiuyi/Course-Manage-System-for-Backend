package com.coursemanage.module.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.group.entity.GroupReport;
import com.coursemanage.module.group.mapper.GroupReportMapper;
import com.coursemanage.module.group.service.GroupReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GroupReportServiceImpl extends ServiceImpl<GroupReportMapper, GroupReport> implements GroupReportService {

    @Override
    public GroupReport uploadReport(GroupReport report) {
        report.setShareTime(LocalDateTime.now()); // 自动填充上传时间
        report.setIsGraded(false); // 初始未评分
        save(report);
        return report;
    }

    @Override
    public boolean scoreReport(GroupReport report) {
        report.setIsGraded(true); // 标记为已评分
        updateById(report);
        return true;
    }
}