package com.coursemanage.module.message.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批量操作请求DTO（批量删除/已读/加星）
 */
@Data
public class BatchOperationRequest {
    @NotEmpty(message = "邮件ID列表不能为空")
    private List<Integer> messageIds;
}