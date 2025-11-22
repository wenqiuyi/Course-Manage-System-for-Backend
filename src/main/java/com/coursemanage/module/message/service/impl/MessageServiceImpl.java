package com.coursemanage.module.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.log.pojo.LogEntity;
import com.coursemanage.module.log.mapper.LogMapper;
import com.coursemanage.module.message.dto.MessageSendRequest;
import com.coursemanage.module.message.dto.MessageVO;
import com.coursemanage.module.message.entity.Message;
import com.coursemanage.module.message.mapper.MessageMapper;
import com.coursemanage.module.message.service.MessageService;
import com.coursemanage.module.student.mapper.StudentMapper;
import com.coursemanage.module.teacher.mapper.TeacherMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final LogMapper logMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    // 移除自注入的MessageService，解决循环依赖
    public MessageServiceImpl(LogMapper logMapper,
                              StudentMapper studentMapper,
                              TeacherMapper teacherMapper) {
        this.logMapper = logMapper;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional
    public Message sendMessage(MessageSendRequest request, String senderNo) {
        // 验证接收者是否存在
        boolean receiverExists = false;
        if (studentMapper.getByStudentNo(request.getReceiverNo()) != null) {
            receiverExists = true;
        } else if (teacherMapper.getByTeacherNo(request.getReceiverNo()) != null) {
            receiverExists = true;
        }

        if (!receiverExists) {
            throw new RuntimeException("接收者不存在");
        }

        // 创建消息
        Message message = new Message();
        message.setSenderNo(senderNo);
        message.setReceiverNo(request.getReceiverNo());
        message.setContent(request.getContent());
        message.setFileUrl(request.getFileUrl());
        message.setLinkUrl(request.getLinkUrl());
        message.setStatus("未读");
        message.setSendTime(LocalDateTime.now());

        // 使用MyBatis-Plus的save方法
        this.save(message);

        // 记录日志
        LogEntity logEntity = new LogEntity();
        logEntity.setOperatorSchoolNum(senderNo);
        logEntity.setTargetSchoolNum(request.getReceiverNo());
        logEntity.setDetailInfo("发送消息：" + request.getContent());
        logEntity.setOperationModule("message");
        logEntity.setOperationAction("send");
        logEntity.setOperationTime(new Date());
        logMapper.insert(logEntity);

        return message;
    }

    @Override
    @Transactional
    public List<MessageVO> getMessageList(String receiverNo) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getReceiverNo, receiverNo)
                .orderByDesc(Message::getSendTime);

        List<Message> messages = this.list(queryWrapper);

        // 获取未读消息ID（Integer类型）
        List<Integer> unreadIds = messages.stream()
                .filter(m -> "未读".equals(m.getStatus()))
                .map(Message::getId)
                .collect(Collectors.toList());

        if (!unreadIds.isEmpty()) {
            // 直接调用内部方法，而非代理对象
            this.batchMarkAsRead(unreadIds);
        }

        // 转换为VO
        return messages.stream().map(message -> {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(message, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Integer messageId) {
        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Message::getId, messageId)
                .set(Message::getStatus, "已读");

        this.update(updateWrapper);

        // 记录日志
        Message message = this.getById(messageId);
        if (message != null) {
            LogEntity logEntity = new LogEntity();
            logEntity.setOperatorSchoolNum(message.getReceiverNo());
            logEntity.setDetailInfo("标记消息为已读，ID：" + messageId);
            logEntity.setOperationModule("message");
            logEntity.setOperationAction("read");
            logEntity.setOperationTime(new Date());
            logMapper.insert(logEntity);
        }
    }

    @Override
    public int getUnreadCount(String receiverNo) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getReceiverNo, receiverNo)
                .eq(Message::getStatus, "未读");

        // 将long类型的count结果强转为int
        long countLong = this.count(queryWrapper);
        return (int) countLong; // 强制转换为int
    }

    @Override
    public MessageVO getMessageById(Integer id, String receiverNo) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getId, id)
                .eq(Message::getReceiverNo, receiverNo);

        Message message = this.getOne(queryWrapper);
        if (message == null) {
            return null;
        }

        // 如果消息未读，自动标记为已读
        if ("未读".equals(message.getStatus())) {
            this.markAsRead(id);
        }

        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);
        return vo;
    }

    @Override
    @Transactional
    public void batchMarkAsRead(List<Integer> messageIds) {
        if (messageIds != null && !messageIds.isEmpty()) {
            LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Message::getId, messageIds)
                    .set(Message::getStatus, "已读");

            this.update(updateWrapper);
        }
    }
}