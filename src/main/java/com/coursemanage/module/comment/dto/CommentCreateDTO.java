package com.coursemanage.module.comment.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CommentCreateDTO {
    @NotNull(message = "courseId 不能为空")
    private Integer courseId;

    @NotNull(message = "commenterId 不能为空")
    private String commenterId;

    @NotNull(message = "content 不能为空")
    private String content;
}
