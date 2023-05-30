package com.example.SUSTechNote.exception;

/**
 * 当即将注册的账户已经存在时，该异常将会被抛出
 */
public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
