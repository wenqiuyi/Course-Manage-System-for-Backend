package com.coursemanage.module.group.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("group_applications")
public class GroupApply {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer groupId;              // 小组ID
    private String groupApplicantStudentId;  // 申请人学工号
    private Boolean isPass;              // 是否通过
    private Boolean isRead;              // 是否已审批
}