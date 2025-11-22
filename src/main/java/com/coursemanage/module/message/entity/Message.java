package com.coursemanage.module.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 保持int对应的Integer类型

    @TableField("sender_no")
    private String senderNo;

    @TableField("receiver_no")
    private String receiverNo;

    @TableField("content")
    private String content;

    @TableField("file_url")
    private String fileUrl;

    @TableField("status")
    private String status;

    @TableField("send_time")
    private LocalDateTime sendTime;

    @TableField("link_url")
    private String linkUrl;
}