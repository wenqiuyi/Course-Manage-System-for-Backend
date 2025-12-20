package com.coursemanage.module.coursereport.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("course_report")
public class CourseReport {
    // id
    @TableId(type = IdType.AUTO)
    @JsonIgnore
    @JsonProperty("id")
    private Integer id;
    // 班级id
    @JsonProperty("course_id")
    private Integer courseId;
    // start_week
    @JsonProperty("start_week")
    private Integer startWeek;
    // end_week
    @JsonProperty("end_week")
    private Integer endWeek;
    // report_num
    @JsonProperty("report_num")
    private Integer reportNum;
}
