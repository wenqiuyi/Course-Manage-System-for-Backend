package com.coursemanage.module.message.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * 回复邮件请求DTO
 */
@Data
public class MessageReplyRequest {
    @NotBlank(message = "回复内容不能为空")
    private String content;

    // 新增附件ID列表
    private List<Integer> attachmentIds;
}