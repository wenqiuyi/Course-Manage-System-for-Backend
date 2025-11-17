package com.coursemanage.module.notice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NoticeVO {
    private Integer id;
    private Integer courseId;
    private String teacherNo;
    private String title;
    private String content;
    private LocalDateTime publishTime;
}


