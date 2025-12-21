package com.coursemanage.module.login.controller;

import com.coursemanage.module.login.pojo.User;
import com.coursemanage.module.login.service.LoginRegisterService;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginRegisterController {
    private final LoginRegisterService loginRegisterService;
    @PostMapping("/login")
    public ResponseResult<Map<String, Object>> login(@RequestBody User user) {
        return loginRegisterService.login(user);
    }
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority({'teacher', 'manager'})")
    public ResponseResult<Map<String, Object>> register(@RequestBody User user){
        return loginRegisterService.register(user);
    }
}
