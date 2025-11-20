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
    // 角色
    @JsonProperty("role")
    @ExcelProperty("角色类型")
    private String role;
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
