package com.coursemanage.module.group.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("groups")  // 绑定数据库表名
public class Groups {
    @TableId(type = IdType.AUTO)  // 主键自增
    private Integer id;
    private Integer courseId;     // 课程ID
    private String groupName;     // 小组名称
    private String groupLeaderStudentId;  // 组长学工号
    private LocalDateTime createTime;     // 创建时间
}