package com.coursemanage.module.accountmanage.service;

import com.coursemanage.module.accountmanage.pojo.Account;
import com.coursemanage.module.accountmanage.pojo.ExcelAccount;
import com.coursemanage.pojo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {
    List<Account> getUsers();
    ResponseResult<Void> createUser(Account account);
    ResponseResult<Void> updateUser(Account account);
    ResponseResult<Void> resetUserPassword(Long id);
    ResponseResult<Void> deleteUser(Long id);
    ResponseResult<Void> importUsers(MultipartFile file);
}
