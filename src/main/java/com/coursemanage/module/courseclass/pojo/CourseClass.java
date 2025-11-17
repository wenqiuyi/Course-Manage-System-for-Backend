package com.coursemanage.module.courseclass.pojo;

import com.coursemanage.module.student.pojo.Student;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
// @Entity
// @Table(name="class")
public class CourseClass {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer courseId;
    private String className;
    private String teacherNo;
    private Integer studentCount;
    // 不加注解会报错，这里ai的做法请自行斟酌
    // 如果不需要数据库关系映射，移除这个字段或添加正确的JPA注解
    // 方案1：移除JPA关系映射（推荐）
    // @Transient  // 表示这个字段不持久化到数据库
    private List<Student> students;

    // 方案2：或者正确配置一对多关系
    // @OneToMany(fetch = FetchType.LAZY)
    // @JoinColumn(name = "class_id")
    // private List<Student> students;
}
