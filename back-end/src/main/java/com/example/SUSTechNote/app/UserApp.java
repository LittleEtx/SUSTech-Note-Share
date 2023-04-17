package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
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

    @GetMapping ("doLogin")
    public String doLogin(String username,String password) {
//        String username = jsonpObject.getString("username");
//        String password = jsonpObject.getString("password");
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            System.out.println();
            System.out.println(StpUtil.getPermissionList());
            return "登录成功";
        }
        return "登录失败";
    }


    @PostMapping("/login/password-login")
    public int login(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String rememberMe =  jsonObject.getString("rememberMe");
        System.out.println(email);
        int userID = Integer.parseInt(email.substring(0, 8));

        if (userService.login(userID,password) == 1){
            StpUtil.login(userID);
            System.out.println(StpUtil.getTokenInfo());
            if (rememberMe.equals("1")){
                StpUtil.renewTimeout(604800);
            }
        }
        System.out.println(StpUtil.getTokenTimeout());
        return userService.login(userID,password);
    }

    @PostMapping("login/email-code-login")
    public int loginWithEmail(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        int userID = Integer.parseInt(email.substring(0, 8));
        String verificationCode = jsonObject.getString("verificationCode");
        String rememberMe =  jsonObject.getString("rememberMe");
        if (verificationCode.equals(redisService.getString(email))) {
            StpUtil.login(userID);
            if (userService.findUserById(userID) == null) {
                userService.register(userID, "", email, "SUSTecher");
            }
            if (rememberMe.equals("1")) {
                StpUtil.renewTimeout(604800);
            }
            return 1;
        } else {
            return 0;
        }
    }

    @PostMapping("/sendEmailCode")
    public String sendEmailCode(@RequestBody JSONObject jsonObject) {
        String from = "392546005@qq.com";
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

    @PostMapping("reset-password/confirm-email")
    public String confirmEmail(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        if (userService.findUserByEmail(email) == null) {
            return "邮箱未注册";
        } else {
            return sendEmailCode(jsonObject);
        }
    }

    @PostMapping("reset-password/verify-email")
    public String verifyEmail(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");

        String verificationCode = jsonObject.getString("verificationCode");
        if (verificationCode.equals(redisService.getString(email))) {
            String token = SaTempUtil.createToken(email,900);
            return token;
        } else {
            return "fail";
        }
    }

    @PostMapping("reset-password/reset-password")
    public String resetPassword(@RequestBody JSONObject jsonObject){
        String token = jsonObject.getString("token");
        String password = jsonObject.getString("password");
        if (SaTempUtil.getTimeout(token) > 0){
            String emailValue = SaTempUtil.parseToken(token,String.class);
            User user = userService.findUserByEmail(emailValue);
            if (password.equals(user.getPassword())){
                return "密码与原密码相同，修改失败";
            }
            if(password.length() <= 6){
                return "密码过短，修改失败";
            }
            user.setPassword(password);
            userService.updateUser(user);
            return "密码修改成功";
        }
        return "token已过期，修改失败";
    }

    @PostMapping("logout")
    public int logout(){
        return userService.logout();
    }

    @GetMapping("/redis_test")
    public void test() {
        redisService.put("a", "1", 5);
    }

    @GetMapping("/project_test")
    public int projectTest() {
        return 200;
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
