package com.coursemanage.module.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.group.entity.GroupMember;
import com.coursemanage.module.group.entity.Groups;
import com.coursemanage.module.group.mapper.GroupsMapper;
import com.coursemanage.module.group.service.GroupsService;
import com.coursemanage.module.group.service.IGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {

    @Autowired
    private IGroupMemberService groupMemberService; // 注入成员服务

    @Transactional(rollbackFor = Exception.class) // 事务保证：小组创建和成员添加要么同时成功，要么同时失败
    @Override
    public Groups createGroup(Groups group) {
        // 1. 设置创建时间并保存小组信息
        group.setCreateTime(LocalDateTime.now());
        save(group); // 保存后，数据库自增ID会自动回写到group对象中

        // 2. 自动将创建者（组长）添加为小组成员
        // 校验：避免重复添加（极端情况防护）
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", group.getId())
                .eq("group_member_id", group.getGroupLeaderStudentId());

        if (groupMemberService.count(queryWrapper) == 0) {
            GroupMember leaderMember = new GroupMember();
            leaderMember.setGroupId(group.getId()); // 关联当前创建的小组ID
            leaderMember.setGroupMemberId(group.getGroupLeaderStudentId()); // 组长学工号
            groupMemberService.save(leaderMember); // 保存成员记录
        }

        return group;
    }

    @Override
    public List<Groups> getGroupsByCourseId(Integer courseId) {
        QueryWrapper<Groups> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }
}