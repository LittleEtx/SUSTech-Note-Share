package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

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
    @PostMapping("/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile[] files) {
        System.out.println("The number of files received: " + files.length);
        String url = "http://localhost:8088/api/static/";
        for (MultipartFile file : files) {
            String fileType = file.getContentType();
            System.out.println(fileType);
            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
                return "file not support";
            }
            String fileName = file.getOriginalFilename();
            System.out.println("Saving " + fileName);
            String savingPath = "E:\\SoftwareEngineering\\UserAvatar";
            File folder = new File(savingPath);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
            try {
                file.transferTo(new File(folder, fileName));
                url += fileName;
                userService.updateAvatar(url, 12112628);
            } catch (Exception e) {
                e.printStackTrace();
                return "unknown error";
            }
        }
        System.out.println(url);
        return url;
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

    @SaCheckLogin
    @GetMapping("/get-id")
    public int getID(){
        System.out.println("15");
        System.out.println(StpUtil.getTokenInfo());
        return StpUtil.getLoginIdAsInt();
    }
//    @GetMapping("/get-info")
//    public User getInfo(@RequestBody JSONObject jsonObject){
//        int userID = jsonObject.getInteger("userID");
//        return userService.findUserById(userID);
//    }
    @GetMapping("/get-info")
    public User getInfo(int userID){
        return userService.findUserById(userID);
    }

    @PostMapping("/update-info")
    public int updateInfo(@RequestBody JSONObject jsonObject) {
        User user = userService.findUserById(jsonObject.getInteger("userID"));
        LocalDateTime lastUpdateTime = user.getUpdateTime();
        LocalDateTime now = LocalDateTime.now();
        if (lastUpdateTime == null || lastUpdateTime.plusWeeks(1).isBefore(now)) {
            user.setUserName(jsonObject.getString("userName"));
            user.setDescription(jsonObject.getString("description"));
            user.setGender(jsonObject.getInteger("gender"));
            user.setBirth(jsonObject.getSqlDate("date"));
            user.setUpdateTime(now);
            return userService.updateUser(user);
        } else {
            return 403;
        }
    }
}
