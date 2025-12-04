package com.coursemanage.module.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coursemanage.module.message.entity.MailAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MailAttachmentService extends IService<MailAttachment> {
    // 上传附件
    MailAttachment uploadAttachment(MultipartFile file) throws IOException;

    // 根据ID查询附件
    MailAttachment getAttachmentById(Integer id);
}