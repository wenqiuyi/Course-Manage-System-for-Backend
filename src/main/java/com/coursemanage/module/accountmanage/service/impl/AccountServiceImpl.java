package com.coursemanage.module.accountmanage.service.impl;

import com.coursemanage.module.accountmanage.mapper.AccountMapper;
import com.coursemanage.module.accountmanage.pojo.Account;
import com.coursemanage.module.accountmanage.pojo.ExcelAccount;
import com.coursemanage.module.accountmanage.service.AccountService;
import com.coursemanage.module.accountmanage.util.EasyExcelUtil;
import com.coursemanage.module.log.annotation.LogOperation;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final String DEFAULT_PASSWORD = "123456";
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final EasyExcelUtil easyExcelUtil;
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

    private List<Account> convertFromExcelAccounts(List<ExcelAccount> excelAccounts) {
        String encodedPassword = encoder.encode(DEFAULT_PASSWORD);
        return excelAccounts.stream()
                .map(excelAccount -> {
                    Account account = new Account();
                    account.setSchoolNum(excelAccount.getSchoolNum());
                    account.setPassword(encodedPassword);
                    account.setRole(excelAccount.getRole());
                    account.setPhone(excelAccount.getPhone());
                    account.setEmail(excelAccount.getEmail());
                    account.setStatus(excelAccount.getStatus().equals("启用") ? 1 : 0);
                    return account;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseResult<Void> importUsers(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error(400, "请选择文件");
        }
        String filename = file.getOriginalFilename();
        if (!filename.endsWith(".xlsx") && !filename.endsWith(".xls")) {
            return ResponseResult.error(400, "请上传Excel文件");
        }
        List<ExcelAccount> users;
        try {
            users = easyExcelUtil.readExcel(file, ExcelAccount.class);
        } catch (Exception e) {
            return ResponseResult.error(400, "处理Excel文件失败: " + e.getMessage());
        }
        try{
            List<Account> accounts = convertFromExcelAccounts(users);
            int count = 0;
            for (Account account : accounts) {
                int insertCount = accountMapper.insert(account);
                if (insertCount == 1) {
                    count++;
                }
            }
            return ResponseResult.success(200, "成功导入" + count + "条数据");
        } catch (Exception e) {
            return ResponseResult.error(400, "导入数据失败: " + e.getMessage());
        }
    }
}
