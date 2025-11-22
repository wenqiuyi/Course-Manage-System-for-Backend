package com.coursemanage.module.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanage.module.message.dto.MessageSendRequest;
import com.coursemanage.module.message.dto.MessageVO;
import com.coursemanage.module.message.entity.Message;
import java.util.List;

public interface MessageService extends IService<Message> {
    Message sendMessage(MessageSendRequest request, String senderNo);

    List<MessageVO> getMessageList(String receiverNo);

    void markAsRead(Integer messageId); // 保持Integer类型

    int getUnreadCount(String receiverNo);

    MessageVO getMessageById(Integer id, String receiverNo);

    void batchMarkAsRead(List<Integer> messageIds); // List<Integer>类型
}