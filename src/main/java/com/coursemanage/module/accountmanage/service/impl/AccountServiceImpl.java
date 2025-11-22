package com.coursemanage.module.accountmanage.service.impl;

import com.coursemanage.module.accountmanage.mapper.AccountMapper;
import com.coursemanage.module.accountmanage.pojo.Account;
import com.coursemanage.module.accountmanage.pojo.ExcelAccount;
import com.coursemanage.module.accountmanage.service.AccountService;
import com.coursemanage.module.accountmanage.util.EasyExcelUtil;
import com.coursemanage.module.log.annotation.LogOperation;
import com.coursemanage.module.student.mapper.StudentMapper;
import com.coursemanage.module.student.pojo.Student;
import com.coursemanage.module.teacher.mapper.TeacherMapper;
import com.coursemanage.module.teacher.pojo.Teacher;
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
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
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
        account.setPassword(encoder.encode(account.getPassword()));
        int count = accountMapper.insert(account);
        return count == 1 ? ResponseResult.success() : ResponseResult.error();
    }

    @Override
    public ResponseResult<Void> updateUser(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
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
                    switch (excelAccount.getRole()) {
                        case "学生" -> account.setRole("student");
                        case "教师", "老师" -> account.setRole("teacher");
                        case "管理员" -> account.setRole("manager");
                        default -> account.setRole("unknown");
                    }
                    account.setPhone(excelAccount.getPhone());
                    account.setEmail(excelAccount.getEmail());
                    account.setStatus(excelAccount.getStatus().equals("启用") ? 1 : 0);
                    return account;
                }).collect(Collectors.toList());
    }

    private List<Student> convertFromExcelAccountsToStudents(List<ExcelAccount> excelAccounts) {
        return excelAccounts.stream()
                .filter(excelAccount -> excelAccount.getRole().equals("学生"))
                .map(excelAccount -> {
                    Student student = new Student();
                    student.setAvaUrl(null);
                    student.setName(excelAccount.getName());
                    switch (excelAccount.getGender()) {
                        case "男" -> student.setGender("M");
                        case "女" -> student.setGender("F");
                        default -> student.setGender("U");
                    }
                    student.setStudentNo(excelAccount.getSchoolNum());
                    student.setDepartment(excelAccount.getDepartment());
                    student.setMajor(excelAccount.getMajor());
                    student.setRoleType("student");
                    return student;
                }).collect(Collectors.toList());
    }
    private List<Teacher> convertFromExcelAccountsToTeachers(List<ExcelAccount> excelAccounts) {
        return excelAccounts.stream()
                .filter(excelAccount -> excelAccount.getRole().equals("学生"))
                .map(excelAccount -> {
                    Teacher teacher = new Teacher();
                    teacher.setAvaUrl(null);
                    teacher.setName(excelAccount.getName());
                    switch (excelAccount.getGender()) {
                        case "男" -> teacher.setGender("M");
                        case "女" -> teacher.setGender("F");
                        default -> teacher.setGender("U");
                    }
                    teacher.setTeacherNo(excelAccount.getSchoolNum());
                    return teacher;
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
            List<Student> students = convertFromExcelAccountsToStudents(users);
            for(Student student : students){
                studentMapper.insertStudent(student);
            }
            List<Teacher> teachers = convertFromExcelAccountsToTeachers(users);
            for(Teacher teacher : teachers){
            }
            return ResponseResult.success(200, "成功导入" + count + "条数据");
        } catch (Exception e) {
            return ResponseResult.error(400, "导入数据失败: " + e.getMessage());
        }
    }

    @Override
    public Account selectById(Long id) {
        return accountMapper.selectById(id);
    }

    @Override
    public Account selectBySchoolNum(String schoolNum) {
        return accountMapper.selectBySchoolNum(schoolNum);
    }
}
