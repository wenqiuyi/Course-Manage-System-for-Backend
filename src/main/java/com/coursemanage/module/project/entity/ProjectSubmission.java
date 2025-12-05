package com.coursemanage.module.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("project_submission")
public class ProjectSubmission {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer practiceId;
    private String studentNo;
    private String fileUrl;
    private LocalDateTime submitTime;
    private Integer score;
    private String teacherComment;
}

