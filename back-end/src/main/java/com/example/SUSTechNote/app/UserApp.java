package com.example.SUSTechNote.app;

import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApp {
    @Autowired
    UserService userService;

    @GetMapping("login")
    public int login(int userId, String password) {
        return userService.findUser(userId, password);
    }
}
