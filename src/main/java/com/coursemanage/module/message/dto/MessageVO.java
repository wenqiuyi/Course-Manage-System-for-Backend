package com.coursemanage.module.message.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件展示VO
 */
@Data
public class MessageVO {
    private Integer id;
    private String senderNo;
    private String receiverNo;
    private String subject;
    private String content;
    private String folder; // 文件夹：inbox/sent/drafts/trash
    private Boolean isStar; // 是否星标
    private Boolean isRead; // 是否已读（转换自status字段）
    private Boolean isDraft; // 是否草稿
    private LocalDateTime sendTime;
    private LocalDateTime createTime;
    // 关联附件列表
    private List<MailAttachmentVO> attachments;
}