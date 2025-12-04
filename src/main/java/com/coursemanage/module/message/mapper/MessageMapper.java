package com.coursemanage.module.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.message.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仅继承BaseMapper，使用MyBatis-Plus自动CRUD，无自定义方法
 */

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    // 无需定义任何方法，BaseMapper已提供：
    // insert/deleteById/updateById/selectById/selectList/selectPage 等
}