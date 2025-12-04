package com.coursemanage.module.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coursemanage.pojo.dto.ApiResponse;
import com.coursemanage.module.message.dto.*;
import com.coursemanage.module.message.entity.MailAttachment;
import com.coursemanage.module.message.entity.Message;
import com.coursemanage.module.message.service.MessageService;
import com.coursemanage.module.message.service.MailAttachmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/mail") // 接口路径保持不变，兼容前端调用
@Validated
public class MessageController {

    private final MessageService messageService;
    private final MailAttachmentService mailAttachmentService;

    // 构造器注入
    public MessageController(MessageService messageService, MailAttachmentService mailAttachmentService) {
        this.messageService = messageService;
        this.mailAttachmentService = mailAttachmentService;
    }

    // 获取当前登录用户学工号
    private String getCurrentUserNo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // 假设Security存储的是学工号
    }

    // ========== 一、邮件读取 ==========
    @GetMapping("/inbox")
    public ApiResponse<IPage<MessageVO>> getInbox(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(messageService.getInbox(getCurrentUserNo(), page, size));
    }

    @GetMapping("/sent")
    public ApiResponse<IPage<MessageVO>> getSent(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(messageService.getSent(getCurrentUserNo(), page, size));
    }

    @GetMapping("/drafts")
    public ApiResponse<IPage<MessageVO>> getDrafts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(messageService.getDrafts(getCurrentUserNo(), page, size));
    }

    @GetMapping("/trash")
    public ApiResponse<IPage<MessageVO>> getTrash(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(messageService.getTrash(getCurrentUserNo(), page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<MessageVO> getMessageDetail(@PathVariable Integer id) {
        MessageVO message = messageService.getMessageDetail(id, getCurrentUserNo());
        return message != null ? ApiResponse.success(message) : ApiResponse.error(404, "邮件不存在或无权查看");
    }

    // ========== 二、邮件写入 ==========
    @PostMapping("/send")
    public ApiResponse<Message> sendMessage(@Valid @RequestBody MessageSendRequest request) {
        return ApiResponse.success(messageService.sendMessage(request, getCurrentUserNo()));
    }

    @PostMapping("/reply/{id}")
    public ApiResponse<Message> replyMessage(@PathVariable Integer id,
                                             @Valid @RequestBody MessageReplyRequest request) {
        return ApiResponse.success(messageService.replyMessage(id, request, getCurrentUserNo()));
    }

    @PostMapping("/draft/save")
    public ApiResponse<Message> saveDraft(@Valid @RequestBody DraftSaveRequest request) {
        return ApiResponse.success(messageService.saveDraft(request, getCurrentUserNo()));
    }

    @PutMapping("/draft/update/{id}")
    public ApiResponse<Message> updateDraft(@PathVariable Integer id,
                                            @Valid @RequestBody DraftSaveRequest request) {
        return ApiResponse.success(messageService.updateDraft(id, request, getCurrentUserNo()));
    }

    // ========== 三、状态更新 ==========
    @PutMapping("/{id}/read")
    public ApiResponse<String> markAsRead(@PathVariable Integer id) {
        messageService.markAsRead(id, getCurrentUserNo());
        // 移除null参数，直接调用仅带消息的success方法
        return ApiResponse.success("标记已读成功");
    }

    @PutMapping("/{id}/unread")
    public ApiResponse<String> markAsUnread(@PathVariable Integer id) {
        messageService.markAsUnread(id, getCurrentUserNo());
        return ApiResponse.success("标记未读成功");
    }

    @PutMapping("/{id}/star")
    public ApiResponse<String> starMessage(@PathVariable Integer id) {
        messageService.starMessage(id, getCurrentUserNo());
        return ApiResponse.success("加星成功");
    }

    @PutMapping("/{id}/unstar")
    public ApiResponse<String> unstarMessage(@PathVariable Integer id) {
        messageService.unstarMessage(id, getCurrentUserNo());
        return ApiResponse.success("取消星标成功");
    }

    // ========== 四、删除与恢复 ==========
    @PutMapping("/{id}/delete")
    public ApiResponse<String> deleteToTrash(@PathVariable Integer id) {
        messageService.deleteToTrash(id, getCurrentUserNo());
        return ApiResponse.success("已移至垃圾箱");
    }

    @PutMapping("/{id}/restore")
    public ApiResponse<String> restoreFromTrash(@PathVariable Integer id) {
        messageService.restoreFromTrash(id, getCurrentUserNo());
        return ApiResponse.success("恢复成功");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePermanently(@PathVariable Integer id) {
        messageService.deletePermanently(id, getCurrentUserNo());
        return ApiResponse.success("永久删除成功");
    }

    // ========== 五、批量操作 ==========
    @PutMapping("/deleteBatch")
    public ApiResponse<String> batchDelete(@Valid @RequestBody BatchOperationRequest request) {
        messageService.batchDelete(request.getMessageIds(), getCurrentUserNo());
        return ApiResponse.success("批量删除成功");
    }

    @PutMapping("/readBatch")
    public ApiResponse<String> batchRead(@Valid @RequestBody BatchOperationRequest request) {
        messageService.batchRead(request.getMessageIds(), getCurrentUserNo());
        return ApiResponse.success("批量设为已读成功");
    }

    @PutMapping("/starBatch")
    public ApiResponse<String> batchStar(@Valid @RequestBody BatchOperationRequest request) {
        messageService.batchStar(request.getMessageIds(), getCurrentUserNo());
        return ApiResponse.success("批量加星成功");
    }

    // ========== 六、附件功能 ==========
    @PostMapping("/attachment/upload")
    public ApiResponse<MailAttachment> uploadAttachment(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(mailAttachmentService.uploadAttachment(file));
    }

    @GetMapping("/attachment/{id}")
    public ApiResponse<MailAttachment> downloadAttachment(@PathVariable Integer id) {
        return ApiResponse.success(mailAttachmentService.getAttachmentById(id));
    }

    // ========== 七、查询搜索 ==========
    @GetMapping("/search")
    public ApiResponse<IPage<MessageVO>> searchMessage(@RequestParam String keyword,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(messageService.searchMessage(getCurrentUserNo(), keyword, page, size));
    }

    @GetMapping("/filter")
    public ApiResponse<IPage<MessageVO>> filterMessage(@RequestParam String folder,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(messageService.filterMessage(getCurrentUserNo(), folder, page, size));
    }

    // ========== 八、统计类 ==========
    @GetMapping("/unread/count")
    public ApiResponse<Integer> getUnreadCount() {
        return ApiResponse.success(messageService.getUnreadCount(getCurrentUserNo()));
    }

    // ========== 九、用户校验 ==========
    @GetMapping("/user/check")
    public ApiResponse<Boolean> checkUserExists(@RequestParam String username) {
        return ApiResponse.success(messageService.checkUserExists(username));
    }
}