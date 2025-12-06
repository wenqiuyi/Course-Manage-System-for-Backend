package com.coursemanage.module.comment.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CommentCreateDTO {
    @NotNull(message = "courseId 不能为空")
    private Integer courseId;

    @NotNull(message = "commenterNo 不能为空")
    private String commenterNo;

    @NotNull(message = "content 不能为空")
    private String content;
}
