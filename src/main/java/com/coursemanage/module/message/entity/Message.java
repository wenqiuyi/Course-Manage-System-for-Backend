package com.coursemanage.module.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库message表（邮件核心表）
 */
@Data
@TableName("message") // 对应数据库表名
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sender_no") // 发送者学工号（关联user表school_num）
    private String senderNo;

    @TableField("receiver_no") // 接收者学工号（关联user表school_num）
    private String receiverNo;

    @TableField("content") // 邮件正文
    private String content;

    @TableField("subject") // 邮件标题（新增字段）
    private String subject;

    @TableField("file_url") // 兼容原有字段（废弃，新附件用mail_attachment表）
    private String fileUrl;

    @TableField("status") // 已读/未读（0=未读，1=已读，数据库存储"未读"/"已读"）
    private String status;

    @TableField("folder") // 文件夹：inbox/sent/drafts/trash
    private String folder;

    @TableField("is_star") // 是否星标（0=否，1=是）
    private Boolean isStar;

    @TableField("is_draft") // 是否草稿（0=否，1=是）
    private Boolean isDraft;

    @TableField("send_time") // 发送时间（草稿可为当前时间）
    private LocalDateTime sendTime;

    @TableField("link_url") // 兼容原有字段（邮件附加链接）
    private String linkUrl;

    @TableField("create_time") // 创建时间（草稿用）
    private LocalDateTime createTime;

    @TableField("update_time") // 更新时间（草稿用）
    private LocalDateTime updateTime;
}