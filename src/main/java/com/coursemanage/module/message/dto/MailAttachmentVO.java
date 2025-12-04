package com.coursemanage.module.message.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 附件展示VO
 */
@Data
public class MailAttachmentVO {
    private Integer id;
    private Integer messageId; // 关联message表ID
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private LocalDateTime uploadTime;
}