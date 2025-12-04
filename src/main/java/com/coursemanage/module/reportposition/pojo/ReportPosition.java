package com.coursemanage.module.reportposition.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("report_position")
public class ReportPosition {
    // id
    @JsonProperty("id")
    @TableId(type = IdType.AUTO)
    private Integer id;
    // course_id
    @JsonProperty("course_id")
    private Integer courseId;
    // school_num
    @JsonProperty("school_num")
    private String schoolNum;
    // week
    @JsonProperty("week")
    private Integer week;
    // position
    @JsonProperty("position")
    private Integer position;
}
