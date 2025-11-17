package com.coursemanage.module.group.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("share_reports") // 对应数据库表名share_reports
public class GroupReport {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer courseId;
    private Integer groupId;
    private String shareUrl; // 对应数据库字段shareUrl
    private LocalDateTime shareTime; // 对应数据库字段shareTime
    private Integer shareGrade; // 对应数据库字段shareGrade
    private Boolean isGraded; // 对应数据库字段isGraded
    private String comment; // 评语
}