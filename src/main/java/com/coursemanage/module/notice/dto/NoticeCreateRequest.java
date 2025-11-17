package com.coursemanage.module.notice.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class NoticeCreateRequest {
    @NotNull(message = "课程ID不能为空")
    private Integer courseId;

    @NotBlank(message = "通知标题不能为空")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    private String content;
}


