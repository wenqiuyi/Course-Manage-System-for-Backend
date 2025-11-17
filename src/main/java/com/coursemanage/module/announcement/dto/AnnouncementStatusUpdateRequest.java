package com.coursemanage.module.announcement.dto;
import lombok.Data;
@Data
public class AnnouncementStatusUpdateRequest {
    private Long id;
    private String status;
}