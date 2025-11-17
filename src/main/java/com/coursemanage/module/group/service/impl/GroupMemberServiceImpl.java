package com.coursemanage.module.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.group.entity.GroupMember;
import com.coursemanage.module.group.mapper.GroupMemberMapper;
import com.coursemanage.module.group.service.IGroupMemberService;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements IGroupMemberService {
}