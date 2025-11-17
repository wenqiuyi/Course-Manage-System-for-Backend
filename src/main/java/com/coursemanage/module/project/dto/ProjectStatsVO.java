package com.coursemanage.module.project.dto;

import lombok.Data;

@Data
public class ProjectStatsVO {
    private Long totalProjects; // 总项目数
    private Long submittedProjects; // 已提交项目数
    private Long gradedProjects; // 已评分项目数
    private Long pendingProjects; // 待提交项目数
}


