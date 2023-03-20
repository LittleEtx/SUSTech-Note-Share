package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public int uploadAvatar(@RequestParam("avatar") MultipartFile[] files) {
        System.out.println("The number of files received: " + files.length);
        for (MultipartFile file : files) {
            String fileType = file.getContentType();
            System.out.println(fileType);
            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
                return 406;
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
                String filePath = savingPath + "\\" + fileName;
                System.out.println(filePath);
            } catch (Exception e) {
                e.printStackTrace();
                return 500;
            }
        }
        return 200;
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
