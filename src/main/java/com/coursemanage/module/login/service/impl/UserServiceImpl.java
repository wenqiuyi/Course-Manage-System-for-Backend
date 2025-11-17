package com.coursemanage.module.login.service.impl;

import com.coursemanage.module.login.mapper.UserMapper;
import com.coursemanage.module.login.pojo.User;
import com.coursemanage.module.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    @Override
    public User getUserBySchoolNum(String schoolNum) {
        return userMapper.selectBySchoolNum(schoolNum);
    }
}
