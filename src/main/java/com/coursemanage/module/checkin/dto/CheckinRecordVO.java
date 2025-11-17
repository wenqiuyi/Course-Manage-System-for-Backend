package com.coursemanage.module.checkin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CheckinRecordVO {
    private Integer id;
    private Integer checkinId;
    private String studentNo;
    private Boolean status; // 1=已签到 0=缺勤
    private LocalDateTime checkinTime;
    private String studentName; // 学生姓名
}


