package com.coursemanage.module.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.group.entity.GroupApply;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupApplyMapper extends BaseMapper<GroupApply> {
    // 自动继承 CRUD 方法，操作 group_applications 表
}