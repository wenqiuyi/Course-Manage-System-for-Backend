package com.coursemanage.module.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.group.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    // 自动继承 CRUD 方法，操作 group_members 表
}