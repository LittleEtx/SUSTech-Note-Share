package com.example.SUSTechNote.exception;

public class GroupNotExistException extends RuntimeException{
    public GroupNotExistException(String message) {
        super(message);
    }
}
