package com.coursemanage.module.courseclass.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CourseClassCreateDTO {
    @NotNull(message = "courseId 不能为空")
    private Integer courseId;

    @NotNull(message = "className 不能为空")
    private String className;

    @NotNull(message = "teacherNo 不能为空")
    private String teacherNo;

    private List<String> studentList;
}
