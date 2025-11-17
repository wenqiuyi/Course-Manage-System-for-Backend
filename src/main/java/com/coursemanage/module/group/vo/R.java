package com.coursemanage.module.group.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code = 200; // 成功状态码
    private String message = "success";
    private T data;

    // 成功响应
    public static <T> R<T> success(T data) {
        return new R<>(200, "success", data);
    }

    // 错误响应
    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message, null);
    }
}