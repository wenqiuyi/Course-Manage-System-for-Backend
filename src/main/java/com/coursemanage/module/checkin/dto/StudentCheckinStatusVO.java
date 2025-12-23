package com.coursemanage.module.checkin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentCheckinStatusVO {
    private Integer checkinId;
    private Integer courseId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * true=已签到 false=未签到
     */
    private Boolean status;
    private LocalDateTime checkinTime;
}


