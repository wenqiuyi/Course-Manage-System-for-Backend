package com.coursemanage.module.accountmanage.controller;

import com.coursemanage.module.accountmanage.pojo.Account;
import com.coursemanage.module.accountmanage.service.AccountService;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/account"})
@RequiredArgsConstructor
public class SingleAccountController {
    private final AccountService accountService;
    private String getCurrentSchoolNum(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }
    @GetMapping("/user")
    public Account getUser(){
        return accountService.selectBySchoolNum(getCurrentSchoolNum());
    }
    @PutMapping("/user")
    public ResponseResult<Void> updateUser(@RequestBody Account account){
        Long id = accountService.selectBySchoolNum(getCurrentSchoolNum()).getId();
        account.setId(id);
        account.setSchoolNum(getCurrentSchoolNum());
        return accountService.updateUser(account);
    }
    @PutMapping("/user/password")
    public ResponseResult<Void> resetUserPassword(){
        Long id = accountService.selectBySchoolNum(getCurrentSchoolNum()).getId();
        return accountService.resetUserPassword(id);
    }
    @DeleteMapping("/user")
    public ResponseResult<Void> deleteUser(){
        Long id = accountService.selectBySchoolNum(getCurrentSchoolNum()).getId();
        return accountService.deleteUser(id);
    }
}
