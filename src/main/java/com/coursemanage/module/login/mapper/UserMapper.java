package com.coursemanage.module.login.mapper;

import com.coursemanage.module.login.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> select();
    @Select("select * from user where id=#{id}")
    User selectById(Long id);
    @Select("select * from user where school_num=#{schoolNum}")
    User selectBySchoolNum(String schoolNum);
    @Insert("insert into user(school_num, password, role) values (#{schoolNum},#{password},#{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    @Delete("delete from user where id=#{id}")
    int deleteById(Long id);
}
