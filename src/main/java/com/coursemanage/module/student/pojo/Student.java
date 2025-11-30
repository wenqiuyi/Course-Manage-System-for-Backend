package com.coursemanage.module.student.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
// @Entity
// @Table(name="student")
public class Student {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String avaUrl;
    private String name;
    private String studentNo;
    private String gender; // "M" or "F"
    private String roleType;
    private String department;
    private String major;
    private Integer courseId;
}
