package com.coursemanage.module.login.service.impl;

import com.coursemanage.module.log.annotation.LogOperation;
import com.coursemanage.module.log.mapper.LogMapper;
import com.coursemanage.module.login.mapper.UserMapper;
import com.coursemanage.module.log.pojo.LogEntity;
import com.coursemanage.module.login.pojo.LoginUser;
import com.coursemanage.module.login.pojo.User;
import com.coursemanage.module.login.util.JWTUtil;
import com.coursemanage.pojo.ResponseResult;
import com.coursemanage.module.login.service.LoginRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginRegisterServiceImpl implements LoginRegisterService {
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    @Override
    @LogOperation(
            operator = "#user.getSchoolNum()",
            detailOnSuccess = "'user '+#user.schoolNum+' ('+#result.data['role']+') login successful'",
            detailOnFailure = "'user '+#user.schoolNum+' ('+#result.data['role']+') login failed'",
            module = "login",
            action = "login"
    )
    public ResponseResult<Map<String, Object>> login(User user) {
        String username = user.getSchoolNum();
        String password = user.getPassword();
        if(username==null || username.isEmpty() || password==null || password.isEmpty())
            return ResponseResult.error(400, "用户名或密码不能为空");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            if (authentication.isAuthenticated()) {
                LoginUser principal = (LoginUser) authentication.getPrincipal();
                Map<String, Object> claims = new HashMap<>();
                claims.put("school_num", principal.getUser().getSchoolNum());
                claims.put("role", principal.getUser().getRole());
                String jwt = JWTUtil.generateToken(claims);
                Map<String, Object> bodyData = new HashMap<>();
                bodyData.put("token", jwt);
                bodyData.put("role", principal.getUser().getRole());
                return ResponseResult.success(bodyData);
            }
            return ResponseResult.error(400, "登录失败，请检查用户名或密码");
        } catch (BadCredentialsException e){
            return ResponseResult.error(400, "登录失败，账号或密码错误");
        } catch (UsernameNotFoundException e){
            return ResponseResult.error(400, "登录失败，用户不存在");
        } catch (LockedException e){
            return ResponseResult.error(400, "登录失败，用户被锁");
        } catch (DisabledException e){
            return ResponseResult.error(400, "登录失败，用户被禁用");
        } catch (RuntimeException e){
            return ResponseResult.error(400, "登录失败，未知错误");
        }
    }

    @Override
    public ResponseResult<Map<String, Object>> register(User user) {
        String schoolNum = user.getSchoolNum();
        if(userMapper.selectBySchoolNum(schoolNum)!=null){
            return ResponseResult.error(400, "当前用户已存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        int count = userMapper.insert(user);
        if(count!=1)
            return ResponseResult.error(400, "新增用户失败，请重新尝试");
        else {
            return ResponseResult.success(200, "注册成功，请重新登录");
        }
    }
}
