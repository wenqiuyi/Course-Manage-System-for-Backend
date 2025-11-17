package com.coursemanage.module.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.group.entity.Groups;
import org.apache.ibatis.annotations.Mapper;

// @Mapper 注解：标记这是 MyBatis 的映射接口
// 泛型 <Groups>：指定该接口操作的实体类是 Groups（对应数据库 groups 表）
@Mapper
public interface GroupsMapper extends BaseMapper<Groups> {
    // 无需手动写方法，BaseMapper 已包含常用 CRUD 操作（如 insert、selectById、update 等）
}