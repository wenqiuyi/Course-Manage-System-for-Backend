package com.coursemanage.module.message.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * 发送邮件请求DTO
 */
@Data
public class MessageSendRequest {
    @NotBlank(message = "接收者学工号不能为空")
    private String receiverNo;

    @NotBlank(message = "邮件标题不能为空")
    private String subject;

    @NotBlank(message = "邮件内容不能为空")
    private String content;

    // 附件ID列表（关联mail_attachment表）
    private List<Integer> attachmentIds;
}