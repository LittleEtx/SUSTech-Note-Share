package com.example.SUSTechNote.interfaces;

import com.example.SUSTechNote.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
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

    public static List<UserInterface> fromUsers(Collection<User> users) {
        return users.stream().map(UserInterface::fromUser).toList();
    }
}
