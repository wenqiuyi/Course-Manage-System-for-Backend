package com.coursemanage.module.project.dto;

import lombok.Data;

@Data
public class ProjectStatsVO {
    private Long totalStudents; // 总学生数
    private Long submittedStudents; // 已提交学生数
    private Long gradedStudents; // 已评分学生数
    private Long pendingStudents; // 待提交学生数
}


