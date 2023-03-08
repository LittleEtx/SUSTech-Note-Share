package com.example.SUSTechNote.app;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class UserApp {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public int login(@RequestBody JSONObject jsonpObject) {
        int userID = Integer.parseInt(jsonpObject.getString("userID"));
        String password = jsonpObject.getString("password");
        return userService.login(userID, password);
    }
    @PostMapping("/register")
    public int register(@RequestBody JSONObject jsonpObject){
        int userID = Integer.parseInt(jsonpObject.getString("userID"));
        String password = jsonpObject.getString("password");
        String email = jsonpObject.getString("email");
        String userName = jsonpObject.getString("userName");
        return userService.register(userID,password,email,userName);
    }
    @PostMapping("/updateUser")
    public int update(@RequestBody JSONObject jsonpObject){
        User user = new User();
        user.setUserID(Integer.parseInt(jsonpObject.getString("userID")));
        user.setUserName(jsonpObject.getString("userName"));
        user.setPassword(jsonpObject.getString("password"));
        user.setEmail(jsonpObject.getString("email"));
        user.setSchedule(jsonpObject.getString("schedule"));
        user.setAvatar(jsonpObject.getString("avatar"));
        return userService.updateUser(user);
    }

    @PostMapping("/deleteUser")
    public int deleteUser(@RequestBody JSONObject jsonpObject){
        int userID = Integer.parseInt(jsonpObject.getString("userID"));
        return userService.deleteUser(userID);
    }

    @GetMapping("/findAllUser")
    public List<User> findAll(){
        return userService.findAllUser();
    }
}
