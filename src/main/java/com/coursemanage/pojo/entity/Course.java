package com.coursemanage.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 会与com.coursemanage.module.course.pojo.Course重名冲突
 */
@Data
@TableName("course")
public class Course {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Long classId;
    private String name;
    private String description;
    private String teacherNo;
}

