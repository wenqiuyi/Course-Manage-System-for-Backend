package com.coursemanage.module.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("project_practice")
public class ProjectPractice {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer courseId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 是否为小组项目：0-否，1-是
     */
    private Boolean isGroupProject;
}

