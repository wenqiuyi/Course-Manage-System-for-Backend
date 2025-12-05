package com.coursemanage.module.project.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentProjectSubmissionVO {
    private Integer submissionId;
    private Integer practiceId;
    private Integer courseId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String fileUrl;
    private LocalDateTime submitTime;
    private Integer score;
    private String teacherComment;
}


