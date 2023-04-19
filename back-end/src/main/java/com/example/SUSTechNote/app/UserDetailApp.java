package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserDetailApp {
    private final Logger logger = LoggerFactory.getLogger(UserDetailApp.class);
    private final UserService userService;
    private final StaticPathHelper staticPathHelper;

    //构造函数自动注入，无需标注Autowire
    public UserDetailApp(UserService userService, StaticPathHelper staticPathHelper) {
        this.userService = userService;
        this.staticPathHelper = staticPathHelper;
    }

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

    @SuppressWarnings("ResultOfMethodCallIgnored")  //so that mkdir() method will not report warning
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile file) throws IOException {
        //int id = StpUtil.getLoginIdAsInt();
        int id = 12112628;
        logger.debug("user "+ id + " upload avatar");
        String fileType = file.getContentType();
        if (!"image/jpeg".equals(fileType) && !"image/png".equals(fileType)) {
            return ResponseEntity.badRequest().body("file not support");
        }
        //使用随机UUID作为文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") +
                ("image/jpeg".equals(fileType) ? ".jpg" : ".png");

        String basePath = staticPathHelper.getStaticPath();
        String savingPath = "/user_avatar";
        File folder = new File(basePath, savingPath);
        String url = "/api/static" + savingPath + "/";
        if (!folder.exists()) {
            logger.debug("create dir for saving avatars at " + folder.getAbsolutePath());
            folder.mkdirs();
        }

        file.transferTo(new File(folder, fileName));
        url += fileName;
        userService.updateAvatar(url, id);
        logger.info("user " + id + " update avatar url to: " + url);
        return ResponseEntity.ok().body(url);
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
