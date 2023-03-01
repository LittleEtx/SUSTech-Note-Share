package com.example.SUSTechNote.service;

import org.springframework.stereotype.Service;


@Service
public interface UserService {
    public int findUser(int userId, String password);
    public int register(int userId, String password, String email, String userName);
}
