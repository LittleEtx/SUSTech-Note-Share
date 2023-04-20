package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.UserRepository;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.exception.AccountAlreadyExistException;
import com.example.SUSTechNote.exception.AccountNotExistException;
import com.example.SUSTechNote.service.UserService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final StaticPathHelper staticPathHelper;

    public UserServiceImpl(UserRepository userRepository,
                           StaticPathHelper staticPathHelper) {
        this.userRepository = userRepository;
        this.staticPathHelper = staticPathHelper;
    }

    @Override
    public boolean login(String email, String password, boolean rememberMe) {
        if (userRepository.existsByEmailAndPassword(email, password)){
            doLogin(userRepository.findUserByEmail(email).getUserID(), rememberMe);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void login(String email, boolean rememberMe) {
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new AccountNotExistException("User not found!");
        }
        doLogin(user.getUserID(), rememberMe);
    }

    private void doLogin(Integer userID, boolean rememberMe) {
        logger.info("User " + userID + " logged in");
        if (rememberMe){
            StpUtil.login(userID);
        } else {
            StpUtil.login(userID, false);
        }
    }

    @Override
    public void register(String email, String userName, String password) {
        doRegister(generateNewID(email), email, userName, password);
    }

    @Override
    public void register(String email) {
        int id = generateNewID(email);
        doRegister(id, email, "SUSTecher" + id, "");
    }

    private void doRegister(int id, String email, String userName, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new AccountAlreadyExistException("The email already be register!");
        }
        logger.info("User " + email + " register with uid: " + id);
        User user = new User();
        user.setUserID(id);
        user.setPassword(password);
        user.setEmail(email);
        user.setUserName(userName);
        userRepository.save(user);
    }

    private int generateNewID(String email) {
        try {
            String subStr = email.substring(0, 8);
            int id = Integer.parseInt(subStr);
            // id should be the prefix of email for email type
            // xxxxxxxx@mail.sustech.edu.cn
            if (!hasUser(id)) {
                logger.warn("ID " + id + " for new email " + email + " already exist!");
                return Math.max(30000000, userRepository.getMaxID() + 1);
            }
            return id;
        } catch (NumberFormatException ignored) {
            return Math.max(30000000, userRepository.getMaxID() + 1);
        }
    }

    @Override
    public void updateUser(User user){
        userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Integer userID){
        if (hasUser(userID)){
            userRepository.deleteById(userID);
            return true;
        }
        return false;
    }
    @Override
    public List<User> findAllUser(){
            return userRepository.findAll();
    }

    @Override
    public User findUserById(int userID){
        return userRepository.findUserByUserID(userID);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public boolean hasUser(Integer userID){
        return userRepository.existsByUserID(userID);
    }

    @Override
    public boolean hasUser(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void logout(){
        StpUtil.logout();
    }

    @Override
    public  int resetPassword( ){
        //TODO
        return 0;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public String updateAvatar(int userID, MultipartFile avatar) throws IOException {
        //使用随机UUID作为文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") +
                ("image/jpeg".equals(avatar.getContentType()) ? ".jpg" : ".png");

        String basePath = staticPathHelper.getStaticPath();
        String savingPath = "/user_avatar";
        File folder = new File(basePath, savingPath);
        if (!folder.exists()) {
            logger.debug("create dir for saving avatars at " + folder.getAbsolutePath());
            folder.mkdirs();
        }

        avatar.transferTo(new File(folder, fileName));
        String url = "/api/static" + savingPath + "/" + fileName;
        logger.info("user " + userID + " update avatar url to: " + url);
        userRepository.updateAvatar(url, userID);
        return url;
    }

}
