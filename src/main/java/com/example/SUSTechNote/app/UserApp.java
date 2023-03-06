package com.example.SUSTechNote.app;

import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserApp {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public int login(int userID, String password) {
        return userService.login(userID, password);
    }
    @PostMapping("/register")
    public int register(int userID, String password, String email, String userName){
        return userService.register(userID,password,email,userName);
    }
    @PostMapping("/updateUser")
    public int register(User user){
        return userService.updateUser(user);
    }

    @GetMapping("/deleteUser")
    public int deleteUser(int userID){
        return userService.deleteUser(userID);
    }

    @GetMapping("/findAllUser")
    public List<User> findAll(){
        return userService.findAllUser();
    }
}
