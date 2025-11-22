package com.coursemanage.module.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.message.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    // 无需定义方法，继承BaseMapper后自动获得CRUD功能
}