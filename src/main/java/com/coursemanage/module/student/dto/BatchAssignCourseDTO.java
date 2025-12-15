package com.coursemanage.module.student.dto;

import lombok.Data;
import java.util.List;

@Data
public class BatchAssignCourseDTO {
    private List<String> studentNos; // 学号列表
}
