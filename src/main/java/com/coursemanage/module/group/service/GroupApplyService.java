package com.coursemanage.module.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanage.module.group.entity.GroupApply;

import java.util.List;

public interface GroupApplyService extends IService<GroupApply> {
    GroupApply applyJoinGroup(GroupApply apply);
    boolean approveApply(GroupApply apply);

    List<GroupApply> getApplyList();
}