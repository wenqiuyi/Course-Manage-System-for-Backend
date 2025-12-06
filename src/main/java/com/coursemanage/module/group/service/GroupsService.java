package com.coursemanage.module.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanage.module.group.entity.Groups;

import java.util.List;

public interface GroupsService extends IService<Groups> {
    Groups createGroup(Groups group);

    List<Groups> getGroupsByCourseId(Integer courseId);
}