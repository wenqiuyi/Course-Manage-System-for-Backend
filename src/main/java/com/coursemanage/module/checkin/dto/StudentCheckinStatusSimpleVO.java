package com.coursemanage.module.checkin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentCheckinStatusSimpleVO {
    private Integer checkinId;
    private String studentNo;
    /**
     * true=已签到，false=未签到
     */
    private Boolean status;
    private LocalDateTime checkinTime;
}


