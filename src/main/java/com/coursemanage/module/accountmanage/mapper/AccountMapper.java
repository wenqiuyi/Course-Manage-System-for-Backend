package com.coursemanage.module.accountmanage.mapper;


import com.coursemanage.module.accountmanage.pojo.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountMapper {
    @Select("select * from user")
    List<Account> selectAll();
    @Select("select * from user where id = #{id}")
    Account selectById(Long id);
    @Insert("insert into user (school_num, password, role, phone, email, status) " +
            "values (#{schoolNum}, #{password}, #{role}, #{phone}, #{email}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Account account);
    @Update("update user set " +
            "school_num = #{schoolNum}, " +
            "password = #{password}, " +
            "role = #{role}, " +
            "phone = #{phone}, " +
            "email = #{email}, " +
            "status = #{status} " +
            "where id = #{id}")
    int update(Account account);
    @Update("update user set password = #{password} where id = #{id}")
    int resetPassword(Long id, String password);
    @Delete("delete from user where id = #{id}")
    int deleteById(Long id);
}
