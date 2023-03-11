package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.UserRepository;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public int login(Integer userID, String password) {
        List<User> users = userRepository.findUsersByUserIDAndPassword(userID, password);
        if (users.size() == 1) {
            return 1;
        } else if (users.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    }
    @Override
    public int checkUser(Integer userID){
        List<User> users = userRepository.findUsersByUserID(userID);
        if (users.size() == 1) {
            return 1;
        } else if (users.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    }

    @Override
    public int register(Integer userID, String password, String email, String userName) {
        if (checkUser(userID) == 0 ){
            User user = new User();
            user.setUserID(userID);
            user.setPassword(password);
            user.setEmail(email);
            user.setUserName(userName);
            userRepository.save(user);
            return 1;
        }
        if (checkUser(userID) == 400){
            return 400;
        }
        return 0;
    }

    @Override
    public int updateUser(User user){
        if (checkUser(user.getUserID()) == 1 ){
            userRepository.save(user);
            return 1;
        }
        if (checkUser(user.getUserID()) == 400){
            return 400;
        }
        return 0;
    }
    @Override
    public int deleteUser(Integer userID){
        if (checkUser(userID) == 1){
            userRepository.deleteById(userID);
            return 1;
        }
        return 0;
    }
    @Override
    public List<User> findAllUser(){
            return userRepository.findAll();
    }

    @Override
    public User findUserById(int userID){
        return userRepository.findUserByUserID(userID);
    }

    @Override
    public int logout(){
        StpUtil.logout();
        return 1;
    }

    @Override
    public  int resetPassword( ){
        return 0;
    }
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}