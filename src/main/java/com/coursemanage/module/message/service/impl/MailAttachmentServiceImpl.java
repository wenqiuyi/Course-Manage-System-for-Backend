package com.coursemanage.module.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanage.module.message.entity.MailAttachment;
import com.coursemanage.module.message.mapper.MailAttachmentMapper;
import com.coursemanage.module.message.service.MailAttachmentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 附件Service（纯MyBatis-Plus实现）
 */
@Service
public class MailAttachmentServiceImpl extends ServiceImpl<MailAttachmentMapper, MailAttachment> implements MailAttachmentService {

    @Value("${file.upload.path}")
    private String uploadPath;

    // 项目启动时自动创建上传目录（递归创建多级目录，比如upload/attachments也会自动建）
    @PostConstruct
    public void initUploadDir() {
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs(); // mkdirs() 递归创建（比如upload不存在则建upload，upload/abc不存在则建upload+abc）
            if (created) {
                System.out.println("自动创建上传目录成功：" + dir.getAbsolutePath());
            } else {
                System.err.println("创建上传目录失败：" + dir.getAbsolutePath() + "（请检查路径权限）");
            }
        }
    }


    @Override
    public MailAttachment uploadAttachment(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 创建上传目录
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成唯一文件名
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        String filePath = uploadPath + File.separator + fileName;

        // 保存文件
        file.transferTo(new File(filePath));

        // 保存附件记录（MyBatis-Plus内置save）
        MailAttachment attachment = new MailAttachment();
        attachment.setFileName(originalFileName);
        attachment.setFileUrl(filePath);
        attachment.setFileSize(file.getSize());
        attachment.setUploadTime(LocalDateTime.now());
        this.save(attachment); // 无需自定义insert，直接用save

        return attachment;
    }

    @Override
    public MailAttachment getAttachmentById(Integer id) {
        return this.getById(id); // MyBatis-Plus内置getById
    }
}