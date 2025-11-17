package com.coursemanage.module.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.group.entity.Groups;
import com.coursemanage.module.group.mapper.GroupsMapper;
import com.coursemanage.module.group.service.GroupsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {

    @Override
    public Groups createGroup(Groups group) {
        group.setCreateTime(LocalDateTime.now());
        save(group);
        return group;
    }
}