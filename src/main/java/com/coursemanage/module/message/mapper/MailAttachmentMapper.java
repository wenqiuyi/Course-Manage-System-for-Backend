package com.coursemanage.module.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.message.entity.MailAttachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仅继承BaseMapper，使用MyBatis-Plus自动CRUD
 */
@Mapper
public interface MailAttachmentMapper extends BaseMapper<MailAttachment> {
    // 无自定义方法，完全依赖BaseMapper
}