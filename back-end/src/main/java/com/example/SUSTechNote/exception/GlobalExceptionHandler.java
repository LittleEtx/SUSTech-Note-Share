package com.example.SUSTechNote.exception;

import cn.dev33.satoken.exception.NotLoginException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * 全局异常处理，所有在 Controller 中抛出的异常都会被此类捕获，
 * 便于统一异常处理以及状态码的返回
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获 Sa-token 的 NotLoginException 异常，返回 401 状态码（Not Login）
     */
    @ExceptionHandler(NotLoginException.class)
    public String notLoginException(NotLoginException e) {
        return ResponseEntity.status(401).body(e.getMessage()).toString();
    }

    /**
     * 捕获 IOException 异常，返回 500 状态码（Internal Server Error）
     */
    @ExceptionHandler(IOException.class)
    public String ioException(IOException e) {
        return ResponseEntity.internalServerError().body(e.getMessage()).toString();
    }

}
