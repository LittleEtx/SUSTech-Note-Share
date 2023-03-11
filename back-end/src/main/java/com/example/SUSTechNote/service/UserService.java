package com.example.SUSTechNote.service;

import com.example.SUSTechNote.config.SaTokenConfigure;
import com.example.SUSTechNote.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserService {
    public int login(Integer userID, String password);
    public int register(Integer userID, String password, String email, String userName);

    public int updateUser(User user);

    public int checkUser(Integer userID);

    public int deleteUser(Integer userID);

    public List<User> findAllUser();

    public int logout();

    public int resetPassword();
    public User findUserById(int userID);

    public User findUserByEmail(String email);
}
