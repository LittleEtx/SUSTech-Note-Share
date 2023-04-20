package com.example.SUSTechNote.exception;

/**
 * 当无法找到一个已经注册的账户时（通过ID或者邮箱），抛出该异常
 */
public class AccountNotExistException extends RuntimeException{
    public AccountNotExistException(String message) {
        super(message);
    }
}
