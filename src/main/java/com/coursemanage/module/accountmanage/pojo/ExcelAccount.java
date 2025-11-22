package com.coursemanage.module.accountmanage.pojo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelIgnoreUnannotated
public class ExcelAccount {
    // 用户学工号
    @JsonProperty("school_num")
    @ExcelProperty("学工号")
    private String schoolNum;
    // 姓名
    @JsonProperty("name")
    @ExcelProperty("姓名")
    private String name;
    // 性别
    @JsonProperty("gender")
    @ExcelProperty("性别")
    private String gender;
    // 角色
    @JsonProperty("role")
    @ExcelProperty("角色类型")
    private String role;
    // 院系
    @JsonProperty("department")
    @ExcelProperty("院系")
    private String department;
    // 专业
    @JsonProperty("major")
    @ExcelProperty("专业")
    private String major;
    // 手机号
    @JsonProperty("phone")
    @ExcelProperty("手机号")
    private String phone;
    // 邮箱号
    @JsonProperty("email")
    @ExcelProperty("邮箱")
    private String email;
    // 是否启用 0-不启用 1-启用
    @JsonProperty("status")
    @ExcelProperty("状态")
    private String status;
}
