package com.coursemanage.module.course.pojo;

import com.coursemanage.module.teacher.pojo.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
// @Entity
// @Table(name="course")
public class Course {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 不注释会报错，有待处理
    //private Integer classId; // 可选
    private String name;
    private String description;
    private Boolean commentArea;
    private String acaYear;
    private String semester;
    private String teacherNo;


}
