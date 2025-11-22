package com.coursemanage.module.teacher.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
// @Entity
// @Table(name="teacher")
public class Teacher {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String avaUrl;
    private String name;
    private String teacherNo;
    private String gender;
    private String department;
}
