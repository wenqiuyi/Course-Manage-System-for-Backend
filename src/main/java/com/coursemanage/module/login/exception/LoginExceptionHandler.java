package com.coursemanage.module.login.exception;

import com.coursemanage.pojo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.coursemanage.module.login.controller")
@Slf4j
public class LoginExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        // 可以打印日志：ex.printStackTrace(); 或使用日志框架
        log.error("请求体格式错误：{}", ex.getMessage());
        return ResponseResult.error(400, "请求体格式错误，请传递正确的 JSON 数据（包含 school_num 和 password）");
    }
}
