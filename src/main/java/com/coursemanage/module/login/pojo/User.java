package com.coursemanage.module.login.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // id
    @JsonProperty("id")
    private Long id;
    // 用户学工号
    @JsonProperty("school_num")
    private String schoolNum;
    // 密码
    @JsonProperty("password")
    private String password;
    // 角色
    @JsonProperty("role")
    private String role;
    // 是否启用 0-不启用 1-启用
    @JsonProperty("status")
    private Integer status;
}
