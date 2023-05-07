package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserDetailApp {
    private final Logger logger = LoggerFactory.getLogger(UserDetailApp.class);
    private final UserService userService;

    //构造函数自动注入，无需标注Autowire
    public UserDetailApp(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/updateUser")
    public ResponseEntity<?> update(@RequestBody JSONObject jsonObject) {
        User user = new User();
        user.setUserID(Integer.parseInt(jsonObject.getString("userID")));
        user.setUserName(jsonObject.getString("userName"));
        user.setPassword(jsonObject.getString("password"));
        user.setEmail(jsonObject.getString("email"));
        user.setSchedule(jsonObject.getString("schedule"));
        user.setAvatar(jsonObject.getString("avatar"));
        userService.updateUser(user);
        return ResponseEntity.ok().body("update success");
    }

    //so that mkdir() method will not report warning
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) throws IOException {
        int id = StpUtil.getLoginIdAsInt(); //获取用户ID
        String fileType = avatar.getContentType();
        if (!"image/jpeg".equals(fileType) && !"image/png".equals(fileType)) {
            logger.debug("User avatar upload type not match");
            return ResponseEntity.badRequest().body("file not support");
        }

        String url = userService.updateAvatar(id, avatar);
        return ResponseEntity.ok(url);
    }

    @SaCheckRole("admin")
    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody JSONObject jsonObject) {
        int userID = Integer.parseInt(jsonObject.getString("userID"));
        if (userService.deleteUser(userID)) {
            return ResponseEntity.ok().body("delete user " + userID + " success");
        } else {
            return ResponseEntity.badRequest().body("user " + userID + " not exist!");
        }
    }

    @GetMapping("/findAllUser")
    public List<User> findAll() {
        return userService.findAllUser();
    }

    @SaCheckLogin
    @GetMapping("/get-id")
    public int getID(){
        return StpUtil.getLoginIdAsInt();
    }

    @GetMapping("/get-info")
    public ResponseEntity<?> getInfo(Integer userID){
        if (userID == null) {
            return ResponseEntity.badRequest().body("attribute userID is required");
        }
        User user = userService.findUserById(userID);
        if (user == null) {
            return ResponseEntity.badRequest().body("user not exist!");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("userID",user.getUserID());
        map.put("userName",user.getUserName());
        map.put("email",user.getEmail());
        map.put("avatar",user.getAvatar());
        map.put("description",user.getDescription());
        map.put("gender",user.getGender());
        map.put("birth",user.getBirth());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/update-info")
    public ResponseEntity<?> updateInfo(@RequestBody JSONObject jsonObject) {
        User user = userService.findUserById(StpUtil.getLoginIdAsInt());
        LocalDateTime lastUpdateTime = user.getUpdateTime();
        LocalDateTime now = LocalDateTime.now();
        String newName = jsonObject.getString("userName");
        // 每周只能改一次用户名
        if (!Objects.equals(newName, user.getUserName()) &&
                now.minusWeeks(1).isAfter(lastUpdateTime)) {
            return ResponseEntity.badRequest().body("username can only be changed once a week");
        }
        user.setUserName(newName);
        user.setDescription(jsonObject.getString("description"));
        user.setGender(jsonObject.getInteger("gender"));
        user.setBirth(jsonObject.getSqlDate("date"));
        user.setUpdateTime(now);
        userService.updateUser(user);
        return ResponseEntity.ok().body("update success");
    }
}
