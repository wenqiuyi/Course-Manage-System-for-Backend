package com.coursemanage.module.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.exception.RecipientNotFoundException;
import com.coursemanage.module.login.pojo.User;
import com.coursemanage.module.login.service.UserService;
import com.coursemanage.module.message.dto.*;
import com.coursemanage.module.message.entity.Message;
import com.coursemanage.module.message.entity.MailAttachment;
import com.coursemanage.module.message.mapper.MessageMapper;
import com.coursemanage.module.message.mapper.MailAttachmentMapper;
import com.coursemanage.module.message.service.MessageService;
import com.coursemanage.module.accountmanage.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 纯MyBatis-Plus实现，无自定义SQL/XML
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final MailAttachmentMapper mailAttachmentMapper;
    private final AccountService accountService;

    // 构造器注入：补充AccountService参数
    public MessageServiceImpl(MailAttachmentMapper mailAttachmentMapper, AccountService accountService) {
        this.mailAttachmentMapper = mailAttachmentMapper;
        this.accountService = accountService; // 赋值给成员变量
    }

    // ========== 核心工具方法：构建通用邮件查询条件 ==========
    private LambdaQueryWrapper<Message> buildMessageQueryWrapper(String userNo, String folder, String keyword) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        if ("inbox".equals(folder)) {
            // 收件箱：只能是接收者且文件夹为inbox
            wrapper.eq(Message::getReceiverNo, userNo)
                    .eq(Message::getFolder, "inbox");
        } else if ("sent".equals(folder)) {
            // 已发送：只能是发送者且文件夹为sent
            wrapper.eq(Message::getSenderNo, userNo)
                    .eq(Message::getFolder, "sent");
        } else {
            // 其他文件夹：当前用户是发送者或接收者，且匹配文件夹
            wrapper.and(w -> w.eq(Message::getSenderNo, userNo).or().eq(Message::getReceiverNo, userNo))
                    .eq(Message::getFolder, folder);
        }
        // 关键词过滤（标题/内容/发送者，可选）
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Message::getSubject, keyword)
                    .or().like(Message::getContent, keyword)
                    .or().like(Message::getSenderNo, keyword));
        }
        // 排序：按发送时间降序
        wrapper.orderByDesc(Message::getSendTime);
        return wrapper;
    }

    // ========== 工具方法：Message转MessageVO ==========
    private MessageVO convertToVO(Message message) {
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);
        // 转换status（"已读"/"未读"）为Boolean类型的isRead
        vo.setIsRead("已读".equals(message.getStatus()));
        // 查询并关联附件
        List<MailAttachment> attachments = mailAttachmentMapper.selectList(
                new LambdaQueryWrapper<MailAttachment>().eq(MailAttachment::getMessageId, message.getId())
        );
        vo.setAttachments(attachments.stream().map(att -> {
            MailAttachmentVO attVO = new MailAttachmentVO();
            BeanUtils.copyProperties(att, attVO);
            return attVO;
        }).collect(Collectors.toList()));
        return vo;
    }

    // ========== 工具方法：校验邮件权限 ==========
    private Message getValidMessage(Integer messageId, String userNo) {
        Message message = this.getById(messageId);
        if (message == null) {
            throw new RuntimeException("邮件不存在");
        }
        // 校验是否为发送者或接收者
        if (!userNo.equals(message.getSenderNo()) && !userNo.equals(message.getReceiverNo())) {
            throw new SecurityException("无权操作此邮件");
        }
        return message;
    }

    // ========== 邮件读取（纯MyBatis-Plus分页查询） ==========
    @Override
    public IPage<MessageVO> getInbox(String userNo, int page, int size) {
        return getMessagePage(userNo, "inbox", null, page, size);
    }

    @Override
    public IPage<MessageVO> getSent(String userNo, int page, int size) {
        return getMessagePage(userNo, "sent", null, page, size);
    }

    @Override
    public IPage<MessageVO> getDrafts(String userNo, int page, int size) {
        return getMessagePage(userNo, "drafts", null, page, size);
    }

    @Override
    public IPage<MessageVO> getTrash(String userNo, int page, int size) {
        return getMessagePage(userNo, "trash", null, page, size);
    }

    @Override
    public IPage<MessageVO> searchMessage(String userNo, String keyword, int page, int size) {
        return getMessagePage(userNo, null, keyword, page, size);
    }

    @Override
    public IPage<MessageVO> filterMessage(String userNo, String folder, int page, int size) {
        return getMessagePage(userNo, folder, null, page, size);
    }

    // 通用分页查询方法（纯MyBatis-Plus）
    private IPage<MessageVO> getMessagePage(String userNo, String folder, String keyword, int page, int size) {
        // 1. 构建分页对象
        Page<Message> pageParam = new Page<>(page, size);
        // 2. MyBatis-Plus分页查询Message
        IPage<Message> messagePage = this.page(pageParam, buildMessageQueryWrapper(userNo, folder, keyword));
        // 3. 转换为MessageVO分页对象
        Page<MessageVO> voPage = new Page<>(page, size, messagePage.getTotal());
        voPage.setRecords(messagePage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public MessageVO getMessageDetail(Integer id, String userNo) {

        Message message = getValidMessage(id, userNo);
        // 未读邮件自动标记为已读
        if ("inbox".equals(message.getFolder()) && "未读".equals(message.getStatus())) {
            markAsRead(id, userNo);
        }
        return convertToVO(message);
    }

    // ========== 邮件写入（纯MyBatis-Plus新增/更新） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message sendMessage(MessageSendRequest request, String senderNo) {
        // 1. 校验收件人
        if (!checkUserExists(request.getReceiverNo())) {
            // 抛自定义异常（不再抛普通RuntimeException）
            throw new RecipientNotFoundException("接收者学工号不存在");
        }

        // 2. 检查是否发送给自己
        if (senderNo.equals(request.getReceiverNo())) {
            throw new RuntimeException("不能向自己发送邮件");
        }

        // 2. 构建收件人邮件（inbox，未读）
        Message receiverMsg = new Message();
        receiverMsg.setSenderNo(senderNo);
        receiverMsg.setReceiverNo(request.getReceiverNo());
        receiverMsg.setSubject(request.getSubject());
        receiverMsg.setContent(request.getContent());
        receiverMsg.setFolder("inbox");
        receiverMsg.setIsStar(false);
        receiverMsg.setIsDraft(false);
        receiverMsg.setStatus("未读");
        receiverMsg.setSendTime(LocalDateTime.now());
        receiverMsg.setCreateTime(LocalDateTime.now());
        receiverMsg.setUpdateTime(LocalDateTime.now());
        this.save(receiverMsg); // MyBatis-Plus内置save方法

        // 3. 构建发件人邮件（sent，已读）
        Message senderMsg = new Message();
        BeanUtils.copyProperties(receiverMsg, senderMsg);
        senderMsg.setId(null); // 重置ID，自增
        senderMsg.setFolder("sent");
        senderMsg.setStatus("已读");
        this.save(senderMsg); // MyBatis-Plus内置save方法

        // 4. 关联附件（如有）
        if (request.getAttachmentIds() != null && !request.getAttachmentIds().isEmpty()) {
            for (Integer attId : request.getAttachmentIds()) {
                MailAttachment att = mailAttachmentMapper.selectById(attId);
                if (att != null) {
                    // 关联发件人邮件
                    att.setMessageId(senderMsg.getId());
                    mailAttachmentMapper.updateById(att); // MyBatis-Plus内置updateById
                    // 复制附件关联收件人邮件
                    MailAttachment attCopy = new MailAttachment();
                    BeanUtils.copyProperties(att, attCopy);
                    attCopy.setId(null);
                    attCopy.setMessageId(receiverMsg.getId());
                    mailAttachmentMapper.insert(attCopy); // MyBatis-Plus内置insert
                }
            }
        }

        return senderMsg;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message saveDraft(DraftSaveRequest request, String userNo, Integer draftId) {
        // 如果有draftId则更新，否则创建新草稿
        if (draftId != null) {
            Message draft = getValidMessage(draftId, userNo);
            if (!draft.getIsDraft()) {
                throw new RuntimeException("非草稿邮件无法更新");
            }

            // 更新草稿内容
            draft.setReceiverNo(request.getReceiverNo() == null ? draft.getReceiverNo() : request.getReceiverNo());
            draft.setSubject(request.getSubject() == null ? draft.getSubject() : request.getSubject());
            draft.setContent(request.getContent() == null ? draft.getContent() : request.getContent());
            draft.setUpdateTime(LocalDateTime.now());
            this.updateById(draft);

            // 清空原有附件关联，重新绑定
            if (request.getAttachmentIds() != null) {
                mailAttachmentMapper.delete(
                        new LambdaQueryWrapper<MailAttachment>().eq(MailAttachment::getMessageId, draftId)
                );
                for (Integer attId : request.getAttachmentIds()) {
                    MailAttachment att = mailAttachmentMapper.selectById(attId);
                    if (att != null) {
                        att.setMessageId(draftId);
                        mailAttachmentMapper.updateById(att);
                    }
                }
            }
            return draft;
        } else {
            // 创建新草稿
            Message draft = new Message();
            draft.setSenderNo(userNo);
            draft.setReceiverNo(request.getReceiverNo() == null ? "" : request.getReceiverNo());
            draft.setSubject(request.getSubject() == null ? "" : request.getSubject());
            draft.setContent(request.getContent() == null ? "" : request.getContent());
            draft.setFolder("drafts");
            draft.setIsStar(false);
            draft.setIsDraft(true);
            draft.setStatus("未读");
            draft.setSendTime(LocalDateTime.now());
            draft.setCreateTime(LocalDateTime.now());
            draft.setUpdateTime(LocalDateTime.now());
            this.save(draft);

            // 关联附件
            if (request.getAttachmentIds() != null && !request.getAttachmentIds().isEmpty()) {
                for (Integer attId : request.getAttachmentIds()) {
                    MailAttachment att = mailAttachmentMapper.selectById(attId);
                    if (att != null) {
                        att.setMessageId(draft.getId());
                        mailAttachmentMapper.updateById(att);
                    }
                }
            }
            return draft;
        }
    }

    @Override
    public Message replyMessage(Integer messageId, MessageReplyRequest request, String senderNo) {
        Message originalMsg = getValidMessage(messageId, senderNo);
        // 构建回复请求
        MessageSendRequest sendRequest = new MessageSendRequest();
        sendRequest.setReceiverNo(originalMsg.getSenderNo());
        sendRequest.setSubject("Re: " + originalMsg.getSubject());
        sendRequest.setContent(request.getContent());
        sendRequest.setAttachmentIds(request.getAttachmentIds());
        return sendMessage(sendRequest, senderNo);
    }

    // ========== 状态更新（纯MyBatis-Plus条件更新） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Integer messageId, String userNo) {
        getValidMessage(messageId, userNo); // 权限校验
        // MyBatis-Plus条件更新
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, messageId)
                        .set(Message::getStatus, "已读")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsUnread(Integer messageId, String userNo) {
        getValidMessage(messageId, userNo);
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, messageId)
                        .set(Message::getStatus, "未读")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void starMessage(Integer messageId, String userNo) {
        getValidMessage(messageId, userNo);
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, messageId)
                        .set(Message::getIsStar, true)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unstarMessage(Integer messageId, String userNo) {
        getValidMessage(messageId, userNo);
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, messageId)
                        .set(Message::getIsStar, false)
        );
    }

    // ========== 删除与恢复（纯MyBatis-Plus更新/删除） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteToTrash(Integer messageId, String userNo) {
        getValidMessage(messageId, userNo);
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, messageId)
                        .set(Message::getFolder, "trash")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreFromTrash(Integer messageId, String userNo) {
        Message message = getValidMessage(messageId, userNo);
        // 恢复到原文件夹（发件人→sent，收件人→inbox）
        String targetFolder = message.getSenderNo().equals(userNo) ? "sent" : "inbox";
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .eq(Message::getId, messageId)
                        .set(Message::getFolder, targetFolder)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermanently(Integer messageId, String userNo) {
        getValidMessage(messageId, userNo);
        // 删除邮件
        this.removeById(messageId); // MyBatis-Plus内置removeById
        // 删除关联附件
        mailAttachmentMapper.delete(
                new LambdaQueryWrapper<MailAttachment>().eq(MailAttachment::getMessageId, messageId)
        );
    }

    // ========== 批量操作（纯MyBatis-Plus批量更新） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Integer> messageIds, String userNo) {
        // 批量查询邮件
        List<Message> messages = this.listByIds(messageIds);
        if (messages.size() != messageIds.size()) {
            throw new RuntimeException("存在无效的邮件ID");
        }
        // 逐个校验权限
        for (Message message : messages) {
            if (!userNo.equals(message.getSenderNo()) && !userNo.equals(message.getReceiverNo())) {
                throw new SecurityException("存在无权操作的邮件");
            }
        }
        for (Integer id : messageIds) {
            deleteToTrash(id, userNo);
        }
        this.removeByIds(messageIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRead(List<Integer> messageIds, String userNo) {
        // 批量更新为已读（MyBatis-Plus批量条件更新）
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .in(Message::getId, messageIds)
                        .eq(Message::getReceiverNo, userNo) // 仅更新当前用户的收件箱邮件
                        .set(Message::getStatus, "已读")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchStar(List<Integer> messageIds, String userNo) {
        this.update(
                new LambdaUpdateWrapper<Message>()
                        .in(Message::getId, messageIds)
                        .and(w -> w.eq(Message::getSenderNo, userNo).or().eq(Message::getReceiverNo, userNo))
                        .set(Message::getIsStar, true)
        );
    }

    // ========== 统计：未读邮件数量（纯MyBatis-Plus条件查询） ==========
    @Override
    public int getUnreadCount(String userNo) {
        return Math.toIntExact(this.count(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getReceiverNo, userNo)
                        .eq(Message::getStatus, "未读")
                        .eq(Message::getFolder, "inbox")
        )); // MyBatis-Plus内置count方法
    }


    // 在MessageServiceImpl中，修改checkUserExists方法：
    @Override
    public boolean checkUserExists(String username) {
        // 复用现有selectBySchoolNum方法：查询该学工号对应的用户
        // 若返回非null，说明用户存在；否则不存在
        return accountService.selectBySchoolNum(username) != null;
    }
}