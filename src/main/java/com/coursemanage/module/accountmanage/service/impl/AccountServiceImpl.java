package com.coursemanage.module.accountmanage.service.impl;

import com.coursemanage.module.accountmanage.mapper.AccountMapper;
import com.coursemanage.module.accountmanage.pojo.Account;
import com.coursemanage.module.accountmanage.service.AccountService;
import com.coursemanage.module.log.annotation.LogOperation;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final String DEFAULT_PASSWORD = "123456";
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    @LogOperation(
            detailOnSuccess = "获取账号信息表成功",
            detailOnFailure = "获取账号信息表失败",
            module = "account",
            action = "check"
    )
    public List<Account> getUsers() {
        return accountMapper.selectAll();
    }

    @Override
    public ResponseResult<Void> createUser(Account account) {
        String encodedPassword = encoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        int count = accountMapper.insert(account);
        return count == 1 ? ResponseResult.success() : ResponseResult.error();
    }

    @Override
    public ResponseResult<Void> updateUser(Account account) {
        int count = accountMapper.update(account);
        return count == 1 ? ResponseResult.success() : ResponseResult.error();
    }

    @Override
    public ResponseResult<Void> resetUserPassword(Long id) {
        String encodedPassword = encoder.encode(DEFAULT_PASSWORD);
        int count = accountMapper.resetPassword(id, encodedPassword);
        return count == 1 ? ResponseResult.success() : ResponseResult.error();
    }

    @Override
    public ResponseResult<Void> deleteUser(Long id) {
        int count = accountMapper.deleteById(id);
        return count == 1 ? ResponseResult.success() : ResponseResult.error();
    }
}
