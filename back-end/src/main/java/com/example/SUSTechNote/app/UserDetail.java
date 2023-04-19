package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserDetail {

    @Autowired
    UserService userService;
    @Autowired
    ResourceLoader resourceLoader;

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
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file, @RequestParam("userID") String userID) {
        String url = "/api/static/";
        String fileType = file.getContentType();
        System.out.println(fileType);
        if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
            return "file not support";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        try {
            System.out.println("Saving " + newFileName);
            String path = new ApplicationHome(this.getClass()).getDir().getParentFile().getParentFile()
                    .getAbsolutePath() + "/static/UserAvatar";
            System.out.println(path);
            File folder = new File(path);
            if (!folder.isDirectory() || !folder.exists()) {
                folder.mkdirs();
                System.out.println("create folder");
            }
            file.transferTo(new File(folder, newFileName));
            url += newFileName;
            userService.updateAvatar(url, Integer.parseInt(userID));
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown error";
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
    public Map<String,Object> getInfo(int userID){
        User user = userService.findUserById(userID);
        Map<String,Object> map = new HashMap<>();
        map.put("userID",user.getUserID());
        map.put("userName",user.getUserName());
        map.put("email",user.getEmail());
        map.put("avatar",user.getAvatar());
        map.put("description",user.getDescription());
        map.put("gender",user.getGender());
        map.put("birth",user.getBirth());
        return map;
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
