package com.example.SUSTechNote.exception;

/**
 * 当文件不存在时抛出的异常
 */
public class FileNotExistException extends RuntimeException{
    public FileNotExistException(String message) {
        super(message);
    }
}
