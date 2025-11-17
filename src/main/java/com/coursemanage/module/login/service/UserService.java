package com.coursemanage.module.login.service;


import com.coursemanage.module.login.pojo.User;

public interface UserService {
    User getUserBySchoolNum(String schoolNum);
}
