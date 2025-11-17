package com.coursemanage.pojo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserEntityMapper extends BaseMapper<User> {
}


