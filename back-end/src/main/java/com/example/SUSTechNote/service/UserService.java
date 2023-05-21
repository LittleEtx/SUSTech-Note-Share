package com.example.SUSTechNote.service;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public interface UserService {
    boolean login(String email, String password, boolean rememberMe);
    void login(String email, boolean rememberMe);

    void register(String email, String userName, String password);

    List<JSONObject> searchUsersWithLimit(String key, int pageNumber, int pageSize);
    /**
     * Auto register will default username and password
     * @param email email to be register
     */
    void register(String email);

    void updateUser(User user);

    boolean deleteUser(Integer userID);

    List<User> findAllUser();

    void logout();

    int resetPassword();

    boolean hasUser(Integer userID);

    boolean hasUser(String email);
    User findUserById(int userID);

    User findUserByEmail(String email);

    /**
     * 处理头像上传的相关逻辑
     * @param avatar 头像文件
     * @return 头像文件的url
     */
    String updateAvatar(int userID, MultipartFile avatar) throws IOException;
}
