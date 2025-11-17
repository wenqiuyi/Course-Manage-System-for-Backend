package com.coursemanage.module.checkin.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


@Data
public class CheckinStartRequest {
    @NotNull(message = "课程ID不能为空")
    private Integer courseId;

    @NotBlank(message = "签到标题不能为空")
    private String title;

    @NotBlank(message = "签到说明不能为空")
    private String description;

    @NotNull(message = "签到开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "签到结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}


