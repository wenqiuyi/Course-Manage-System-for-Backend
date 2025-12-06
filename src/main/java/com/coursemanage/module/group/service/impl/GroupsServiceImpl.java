package com.coursemanage.module.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.group.entity.Groups;
import com.coursemanage.module.group.mapper.GroupsMapper;
import com.coursemanage.module.group.service.GroupsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {

    @Override
    public Groups createGroup(Groups group) {
        group.setCreateTime(LocalDateTime.now());
        save(group);
        return group;
    }

    @Override
    public List<Groups> getGroupsByCourseId(Integer courseId) {
        QueryWrapper<Groups> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId) // 匹配数据库字段 course_id
                .orderByDesc("create_time"); // 按创建时间倒序
        return baseMapper.selectList(queryWrapper);
    }


}