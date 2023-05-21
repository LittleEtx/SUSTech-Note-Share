package com.example.SUSTechNote.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record UserInterface(
        int userID,
        String userName,
        String email,
        String avatar,
        String description,
        int gender,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date birth
){
    public static UserInterface fromUser(User user) {
        return new UserInterface(
            user.getUserID(),
            user.getUserName(),
            user.getEmail(),
            user.getAvatar(),
            user.getDescription(),
            user.getGender(),
            user.getBirth()
        );
    }

    public static List<UserInterface> fromUserMap(Collection<JSONObject> users) {
        List<UserInterface> userList = new ArrayList<>();
        for (var map : users) {
            var userInfo = new UserInterface(
                    (int) map.get("userid"),
                    (String) map.get("user_name"),
                    (String) map.get("email"),
                    (String) map.get("avatar"),
                    (String) map.get("description"),
                    (int) map.get("gender"),
                    (Date) map.get("birth")
            );
            userList.add(userInfo);
        }
        return userList;
    }

    public static List<UserInterface> fromUsers(Collection<User> users) {
        return users.stream().map(UserInterface::fromUser).toList();
    }
}
