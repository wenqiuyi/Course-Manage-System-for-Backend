package com.coursemanage.module.comment.pojo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
// @Entity
// @Table(name="course_comment")
public class CourseComment {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer courseId;
    private String commenterNo; // student_no
    private String content;
}
