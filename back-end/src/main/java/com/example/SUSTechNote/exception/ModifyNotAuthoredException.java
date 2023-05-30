package com.example.SUSTechNote.exception;

/**
 * 当用户试图修改一个不属于自己的笔记时，该异常将会被抛出
 */
public class ModifyNotAuthoredException extends RuntimeException{
    public ModifyNotAuthoredException(String message) {
        super(message);
    }
}
