package com.coursemanage.module.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanage.module.message.dto.*;
import com.coursemanage.module.message.entity.Message;

import java.util.List;

/**
 * 邮件Service（继承IService，使用MyBatis-Plus自动CRUD）
 */
public interface MessageService extends IService<Message> {
    // 邮件读取
    IPage<MessageVO> getInbox(String userNo, int page, int size);
    IPage<MessageVO> getSent(String userNo, int page, int size);
    IPage<MessageVO> getDrafts(String userNo, int page, int size);
    IPage<MessageVO> getTrash(String userNo, int page, int size);
    MessageVO getMessageDetail(Integer id, String userNo);

    // 邮件写入
    Message sendMessage(MessageSendRequest request, String senderNo);
    Message replyMessage(Integer messageId, MessageReplyRequest request, String senderNo);
    Message saveDraft(DraftSaveRequest request, String userNo,Integer draftId);


    // 状态更新
    void markAsRead(Integer messageId, String userNo);
    void markAsUnread(Integer messageId, String userNo);
    void starMessage(Integer messageId, String userNo);
    void unstarMessage(Integer messageId, String userNo);

    // 删除与恢复
    void deleteToTrash(Integer messageId, String userNo);
    void restoreFromTrash(Integer messageId, String userNo);
    void deletePermanently(Integer messageId, String userNo);

    // 批量操作
    void batchDelete(List<Integer> messageIds, String userNo);
    void batchRead(List<Integer> messageIds, String userNo);
    void batchStar(List<Integer> messageIds, String userNo);

    // 搜索过滤
    IPage<MessageVO> searchMessage(String userNo, String keyword, int page, int size);
    IPage<MessageVO> filterMessage(String userNo, String folder, int page, int size);

    // 统计
    int getUnreadCount(String userNo);

    // 用户校验
    boolean checkUserExists(String username);
}