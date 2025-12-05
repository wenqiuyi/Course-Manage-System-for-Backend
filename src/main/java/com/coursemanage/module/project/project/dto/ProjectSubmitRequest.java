package com.coursemanage.module.project.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ProjectSubmitRequest {
    @NotNull(message = "项目实践ID不能为空")
    private Integer practiceId;

    @NotBlank(message = "学生学号不能为空")
    private String studentNo;

    @NotBlank(message = "文件链接不能为空")
    private String fileUrl;
}


