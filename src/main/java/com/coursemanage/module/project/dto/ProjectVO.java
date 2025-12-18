package com.coursemanage.module.project.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectVO {
    private Integer id;
    private Integer courseId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 是否为小组项目：true-小组项目，false-个人项目
     */
    private Boolean isGroupProject;
}


