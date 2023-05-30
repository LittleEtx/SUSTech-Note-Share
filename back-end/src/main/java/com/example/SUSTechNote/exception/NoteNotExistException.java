package com.example.SUSTechNote.exception;

/**
 * 当试图操作一个不存在的笔记时，该异常将会被抛出
 */
public class NoteNotExistException extends RuntimeException{
    public NoteNotExistException(String message) {
        super(message);
    }
}
