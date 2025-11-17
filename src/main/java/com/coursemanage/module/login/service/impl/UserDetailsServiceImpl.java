package com.coursemanage.module.login.service.impl;


import com.coursemanage.module.login.pojo.LoginUser;
import com.coursemanage.module.login.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserServiceImpl userServiceImpl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userServiceImpl.getUserBySchoolNum(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUser(user);
    }
}
