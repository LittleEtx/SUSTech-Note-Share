package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserDetail {
    @Autowired
    UserService userService;

    @PostMapping("/updateUser")
    public int update(@RequestBody JSONObject jsonObject) {
        User user = new User();
        user.setUserID(Integer.parseInt(jsonObject.getString("userID")));
        user.setUserName(jsonObject.getString("userName"));
        user.setPassword(jsonObject.getString("password"));
        user.setEmail(jsonObject.getString("email"));
        user.setSchedule(jsonObject.getString("schedule"));
        user.setAvatar(jsonObject.getString("avatar"));
        return userService.updateUser(user);
    }

    @SaCheckRole("admin")
    @PostMapping("/deleteUser")
    public int deleteUser(@RequestBody JSONObject jsonObject) {
        int userID = Integer.parseInt(jsonObject.getString("userID"));
        return userService.deleteUser(userID);
    }

    @GetMapping("/findAllUser")
    public List<User> findAll() {
        return userService.findAllUser();
    }

}
