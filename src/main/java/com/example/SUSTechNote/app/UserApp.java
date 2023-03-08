package com.example.SUSTechNote.app;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.MailService;
import com.example.SUSTechNote.service.RedisService;
import com.example.SUSTechNote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserApp {
    private String verificationCode;

    @Autowired
    UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MailService mailService;

    @PostMapping("/login")
    public int login(@RequestBody JSONObject jsonObject) {
        int userID = Integer.parseInt(jsonObject.getString("userID"));
        String password = jsonObject.getString("password");
        return userService.login(userID, password);
    }

    @PostMapping("/sendEmailCode")
    public String sendEmailCode(@RequestBody JSONObject jsonObject) {
        // read properties
        String from = "12011118@mail.sustech.edu.cn";
        String to = jsonObject.getString("email");
        if (!to.endsWith("@mail.sustech.edu.cn") && !to.endsWith("@sustech.edu.cn")) {
            return "The mailbox format is incorrect";
        }
        String subject = "SUSTechNote邮箱注册验证码";
        verificationCode = generateCode();
        String msg = "您的验证码是: \n" + verificationCode + "\n验证码5分钟内有效";
        String state = mailService.sendSimpleMail(from, "SUSTechNote", to, "", subject, msg);
        if (state.equals("success")) {
            redisService.put(to, verificationCode, 5);
            return "success";
        } else {
            System.out.println("Email sending failure");
            return "Email sending failure";
        }
    }

    @PostMapping("/register")
    public int register(@RequestBody JSONObject jsonObject) {
        int userID = Integer.parseInt(jsonObject.getString("userID"));
        String password = jsonObject.getString("password");
        String email = jsonObject.getString("email");
        String userName = jsonObject.getString("userName");
        String verificationCode = jsonObject.getString("verificationCode");
        if (verificationCode.equals(redisService.getString(email))) {
            return userService.register(userID, password, email, userName);
        } else {
            return 0;
        }
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

    @PostMapping("/deleteUser")
    public int deleteUser(@RequestBody JSONObject jsonObject) {
        int userID = Integer.parseInt(jsonObject.getString("userID"));
        return userService.deleteUser(userID);
    }

    @GetMapping("/findAllUser")
    public List<User> findAll() {
        return userService.findAllUser();
    }

    @GetMapping("/test")
    public void test() {
        redisService.put("a", "1", 5);
    }

    public String generateCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
