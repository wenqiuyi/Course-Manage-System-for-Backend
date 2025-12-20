package com.coursemanage.module.project.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ProjectUpdateRequest {
    @NotNull(message = "项目ID不能为空")
    private Integer id;

    @NotBlank(message = "项目标题不能为空")
    private String title;

    @NotBlank(message = "项目描述不能为空")
    private String description;

    /**
     * 是否为小组项目：true-小组项目，false-个人项目
     */
    @NotNull(message = "是否为小组项目不能为空")
    private Boolean isGroupProject;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}

