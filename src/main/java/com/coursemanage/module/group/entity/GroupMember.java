package com.coursemanage.module.group.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("group_members")
public class GroupMember {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer groupId;         // 小组ID（外键关联group表）
    private String groupMemberId;    // 成员学工号
}