package com.coursemanage.module.accountmanage.controller;

import com.coursemanage.module.accountmanage.pojo.Account;
import com.coursemanage.module.accountmanage.service.AccountService;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('manager')")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @GetMapping("/users")
    public List<Account> getUsers(){
        return accountService.getUsers();
    }
    @PutMapping("/users/{id}")
    public ResponseResult<Void> updateUser(@PathVariable Long id, @RequestBody Account account){
        account.setId(id);
        return accountService.updateUser(account);
    }
    @PutMapping("/users/{id}/password")
    public ResponseResult<Void> resetUserPassword(@PathVariable Long id){
        return accountService.resetUserPassword(id);
    }
    @DeleteMapping("/users/{id}")
    public ResponseResult<Void> deleteUser(@PathVariable Long id){
        return accountService.deleteUser(id);
    }
    @PostMapping("/createUser")
    public ResponseResult<Void> createUser(Account account){
        return accountService.createUser(account);
    }
    @PostMapping("/importUsers")
    public ResponseResult<Void> importUsers(List<Account> accounts){
        return ResponseResult.success();
    }
}
