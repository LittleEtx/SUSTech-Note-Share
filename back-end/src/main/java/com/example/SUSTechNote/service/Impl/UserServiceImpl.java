package com.example.SUSTechNote.service.Impl;

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
    public int findUser(int userId, String password) {
        List<User> users = userRepository.findUsersByUserIDAndPassword(userId, password);
        if (users.size() == 1) {
            return 1;
        } else if (users.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    }

    @Override
    public int register(int userId, String password, String email, String userName) {
        return 0;
    }
}
