package com.coursemanage.module.student.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private Integer id;
    private String name;
    private String studentNo;
    private String gender;
    private String roleType;
    private String department;
    private String major;
    private String avaUrl;
}
