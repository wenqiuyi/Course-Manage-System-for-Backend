package com.coursemanage.module.login.service;



import com.coursemanage.module.login.pojo.User;
import com.coursemanage.pojo.ResponseResult;

import java.util.Map;

public interface LoginRegisterService {
    ResponseResult<Map<String, Object>> login(User user);
    ResponseResult<Map<String, Object>> register(User user);
}
