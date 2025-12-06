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
    // private Integer classId; // 可选
    private String name;
    private String description;
    private Boolean commentArea;
    private String acaYear;
    private String semester;
    private String teacherNo;

    public void setTeacher(Teacher t) {

    }
    // 当返回课程详情时，可填充 teacher 信息（组合）
    // 不加注解会报错，这里ai的做法请自行斟酌
    // 修复：添加@Transient注解，表示不映射到数据库
    // @Transient
    // private Teacher teacher;
}
