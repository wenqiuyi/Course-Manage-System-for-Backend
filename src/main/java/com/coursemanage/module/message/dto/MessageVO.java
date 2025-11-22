package com.coursemanage.module.message.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Integer id; // 对应int类型
    private String senderNo;
    private String receiverNo;
    private String content;
    private String fileUrl;
    private String status;
    private LocalDateTime sendTime;
    private String linkUrl;
}