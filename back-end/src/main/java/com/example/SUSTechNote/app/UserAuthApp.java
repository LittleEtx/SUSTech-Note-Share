package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.UserService;
import com.example.SUSTechNote.util.EmailServiceHelper;
import com.example.SUSTechNote.util.RedisServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class UserAuthApp {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final RedisServiceHelper redisService;
    private final EmailServiceHelper mailService;

    public UserAuthApp(UserService userService,
                       RedisServiceHelper redisService,
                       EmailServiceHelper mailService) {
        this.userService = userService;
        this.redisService = redisService;
        this.mailService = mailService;
    }

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
    public ResponseEntity<?> login(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        boolean rememberMe = Objects.requireNonNullElse(
                jsonObject.getBoolean("rememberMe"), false);
        logger.debug("user try login with email: " + email + " and password: " + password + ", rememberMe = " + rememberMe);
        if (userService.login(email, password, rememberMe)) {
            SaTokenInfo token = StpUtil.getTokenInfo();
            return ResponseEntity.ok(token.tokenName + ": " + token.tokenValue);
        } else {
            return ResponseEntity.badRequest().body("Wrong email or password");
        }
    }

    @PostMapping("login/email-code-login")
    public ResponseEntity<?> loginWithEmail(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        String verificationCode = jsonObject.getString("verificationCode");
        boolean rememberMe = Objects.requireNonNullElse(
                jsonObject.getBoolean("rememberMe"), false); //if para not set, default false
        if (Objects.equals(verificationCode, redisService.getString(email))) {
            // successful login
            if (!userService.hasUser(email)) {
                userService.register(email);
            }
            userService.login(email, rememberMe);
            SaTokenInfo token = StpUtil.getTokenInfo();
            return ResponseEntity.ok(token.tokenName + ": " + token.tokenValue);
        } else {
            return ResponseEntity.badRequest().body("Wrong email or verification code");
        }
    }

    @PostMapping("/send-email-code")
    public ResponseEntity<?>  sendEmailCode(@RequestBody JSONObject jsonObject) {
        String userEmail = jsonObject.getString("email");
        if (userEmail == null ||
                !(userEmail.toLowerCase().endsWith("@mail.sustech.edu.cn")
                        || userEmail.toLowerCase().endsWith("@sustech.edu.cn"))) {
            logger.debug("The mailbox format is incorrect: " + userEmail);
            return ResponseEntity.badRequest().body("The mailbox format is incorrect");
        }
        // TODO：对发送的频次进行限制以防止DDoS攻击
        String subject = "SUSTechNote邮箱注册验证码";
        String verificationCode = generateCode();
        String msg = "您的验证码是: \n" + verificationCode + "\n验证码5分钟内有效";
        if (mailService.sendSimpleMail("SUSTechNote", userEmail, "", subject, msg)) {
            redisService.put(userEmail, verificationCode, 5);
            logger.debug("Verification code " + verificationCode + " has been successfully sent");
            return ResponseEntity.ok("Verification code has successfully sent to " + userEmail);
        } else {
            logger.error("Email sending failure! Please check email setting");
            return ResponseEntity.internalServerError().body("Email sending failure");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody JSONObject jsonObject) {
        String password = jsonObject.getString("password");
        String email = jsonObject.getString("email");
        String userName = jsonObject.getString("userName");
        String verificationCode = jsonObject.getString("verificationCode");
        if (Objects.equals(verificationCode, redisService.getString(email))) {
            userService.register(email, userName, password);
            return ResponseEntity.ok("Register successful");
        } else {
            return ResponseEntity.badRequest().body("Wrong email or verification code");
        }
    }

    @PostMapping("reset-password/verify-email")
    public ResponseEntity<?> confirmEmail(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        if (userService.findUserByEmail(email) == null) {
            return ResponseEntity.badRequest().body("Email not registered");
        } else {
            return sendEmailCode(jsonObject);
        }
    }

    @PostMapping("reset-password/confirm-email")
    public ResponseEntity<?> verifyEmail(@RequestBody JSONObject jsonObject) {
        String email = jsonObject.getString("email");
        String verificationCode = jsonObject.getString("verificationCode");
        if (Objects.equals(verificationCode, redisService.getString(email))) {
            //token 的有效期为15分钟
            return ResponseEntity.ok(SaTempUtil.createToken(email,900));
        } else {
            return ResponseEntity.badRequest().body("Wrong email or verification code");
        }
    }

    @PostMapping("reset-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody JSONObject jsonObject){
        String token = jsonObject.getString("token");
        String password = jsonObject.getString("password");
        if (SaTempUtil.getTimeout(token) < 0) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
        if (password == null || password.length() <= 6) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
        String emailValue = SaTempUtil.parseToken(token,String.class);
        User user = userService.findUserByEmail(emailValue);
        if (password.equals(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password cannot be identical with the previous one");
        }
        user.setPassword(password);
        userService.updateUser(user);
        SaTempUtil.deleteToken(token); //销毁该token
        return ResponseEntity.ok("Successfully modify user password");
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(){
        userService.logout();
        return ResponseEntity.ok("logout success");
    }

    private String generateCode() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
