package com.coursemanage.module.message.dto;

import lombok.Data;
import java.util.List;

/**
 * 保存/更新草稿请求DTO
 */
@Data
public class DraftSaveRequest {
    // 草稿接收者（可为空）
    private String receiverNo;
    // 邮件标题
    private String subject;
    // 邮件内容
    private String content;
    // 附件ID列表
    private List<Integer> attachmentIds;
}