package com.coursemanage.module.checkin.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CheckinSubmitRequest {
    @NotNull(message = "签到ID不能为空")
    private Integer checkinId;

    @NotBlank(message = "学生学号不能为空")
    private String studentNo;
}


