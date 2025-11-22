package com.coursemanage.module.message.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class MessageSendRequest {
    @NotBlank(message = "接收者学工号不能为空")
    private String receiverNo;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    private String fileUrl;
    private String linkUrl;
}