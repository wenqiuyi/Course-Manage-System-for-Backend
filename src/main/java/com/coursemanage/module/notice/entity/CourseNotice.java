package com.coursemanage.module.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course_notice")
public class CourseNotice {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer courseId;
    private String teacherNo;
    private String title;
    private String content;
    private LocalDateTime publishTime;
}

