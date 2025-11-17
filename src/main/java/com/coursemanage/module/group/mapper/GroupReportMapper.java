package com.coursemanage.module.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.group.entity.GroupReport;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupReportMapper extends BaseMapper<GroupReport> {
    // 自动继承 CRUD 方法，操作 share_reports 表
}