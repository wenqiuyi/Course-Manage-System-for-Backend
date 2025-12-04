package com.coursemanage.module.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库mail_attachment表（邮件附件表）
 */
@Data
@TableName("mail_attachment") // 精准对应数据库表名
public class MailAttachment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("message_id") // 关联message表的ID（核心调整：原mail_id → message_id）
    private Integer messageId;

    @TableField("file_name") // 附件原始文件名
    private String fileName;

    @TableField("file_url") // 附件存储路径
    private String fileUrl;

    @TableField("file_size") // 附件大小（字节）
    private Long fileSize;

    @TableField("upload_time") // 上传时间
    private LocalDateTime uploadTime;
}