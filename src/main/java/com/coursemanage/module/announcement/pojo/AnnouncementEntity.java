package com.coursemanage.module.announcement.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementEntity {
    private Long id;
    private String title;
    private String content;
    @JsonProperty("publish_time")
    private Date publishTime;
    private Date deadline;
    private Integer level;
    private String status;
}
