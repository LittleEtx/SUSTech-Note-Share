package com.example.SUSTechNote.exception;

import cn.dev33.satoken.exception.NotLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;
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
    public ResponseEntity<?> notLoginException(NotLoginException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * 捕获 IOException 异常，返回 500 状态码（Internal Server Error）
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException e) {
        return ResponseEntity.internalServerError()
                .body("An error happens in the server: " + e.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not exist");
    }

    @ExceptionHandler(NoteNotExistException.class)
    public ResponseEntity<?> noteNotExistHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not exist");
    }

    @ExceptionHandler(ModifyNotAuthoredException.class)
    public ResponseEntity<?> expHandler() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of this note");
    }

}
