package com.coursemanage.module.message.controller;

import com.coursemanage.pojo.dto.ApiResponse;
import com.coursemanage.module.message.dto.MessageSendRequest;
import com.coursemanage.module.message.dto.MessageVO;
import com.coursemanage.module.message.entity.Message;
import com.coursemanage.module.message.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/message")
@Validated
public class MessageController {

    private final MessageService messageService;

    // 构造器注入
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 获取当前登录用户学工号
     */
    private String getCurrentUserNo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public ApiResponse<Message> sendMessage(@Valid @RequestBody MessageSendRequest request) {
        try {
            String senderNo = getCurrentUserNo();
            Message message = messageService.sendMessage(request, senderNo);
            return ApiResponse.success("消息发送成功", message);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/list/read/{id}")
    public ApiResponse<MessageVO> getMessageDetail(@PathVariable Integer id) {
        try {
            String receiverNo = getCurrentUserNo();
            MessageVO message = messageService.getMessageById(id, receiverNo);
            if (message == null) {
                return ApiResponse.error(404, "消息不存在或无权查看");
            }
            return ApiResponse.success(message);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    /**
     * 获取收件箱消息
     */
    @GetMapping("/list")
    public ApiResponse<List<MessageVO>> getMessageList() {
        try {
            String receiverNo = getCurrentUserNo();
            List<MessageVO> messages = messageService.getMessageList(receiverNo);
            return ApiResponse.success(messages);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取所有消息（管理员）
     */
    @GetMapping("/messages")
    public ApiResponse<List<MessageVO>> getAllMessages() {
        try {
            String currentUserNo = getCurrentUserNo();
            // 实际需添加管理员权限验证
            List<MessageVO> messages = messageService.getMessageList(currentUserNo);
            return ApiResponse.success(messages);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{id}")
    public ApiResponse<String> markAsRead(@PathVariable Integer id) {
        try {
            messageService.markAsRead(id);
            return ApiResponse.success(null, "消息已标记为已读");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    public ApiResponse<Integer> getUnreadCount() {
        try {
            String userNo = getCurrentUserNo();
            int count = messageService.getUnreadCount(userNo);
            return ApiResponse.success(count);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}