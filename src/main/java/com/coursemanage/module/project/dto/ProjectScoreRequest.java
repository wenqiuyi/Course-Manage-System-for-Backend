package com.coursemanage.module.project.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ProjectScoreRequest {
    @NotNull(message = "项目实践ID不能为空")
    private Integer practiceId;

    @NotBlank(message = "学生学号不能为空")
    private String studentNo;

    @NotNull(message = "分数不能为空")
    private Integer score;
}


