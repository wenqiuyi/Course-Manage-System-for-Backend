package com.coursemanage.module.group.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanage.module.group.entity.GroupMember;
import com.coursemanage.module.group.service.IGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupMemberController {

    @Autowired
    private IGroupMemberService groupMemberService;

    /**
     * 根据小组ID查询成员列表
     */
    @GetMapping("/members/{groupId}")
    public List<GroupMember> getGroupMembers(@PathVariable Integer groupId) {
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        return groupMemberService.list(queryWrapper);
    }
}