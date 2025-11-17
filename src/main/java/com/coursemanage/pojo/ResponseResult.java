package com.coursemanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {
    private Integer code;
    private String message;
    private T data;
    public static <T> ResponseResult<T> success() {
        return success(null);
    }
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }
    public static <T> ResponseResult<T> success(Integer code, String message) {
        return new ResponseResult<>(code, message, null);
    }
    public static <T> ResponseResult<T> error(){
        return error(400, "操作失败");
    }

    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code, message, null);
    }
}