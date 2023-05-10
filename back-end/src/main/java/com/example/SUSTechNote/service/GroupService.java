package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupService {
    List<Group> loadJoinedGroup(int userID);

    String findUserNameByUserID(int userID);

    void createGroup(int userID, String groupName, String groupDescription, String createTime, String groupOwnerName);

    List<Group> loadEnjoinedGroup(int userID);

    void joinGroup(int userID, int groupID);

    void quitGroup(int userID, int groupID);

    void deleteGroup(int userID, int groupID);

    void updateGroup(int groupID, String groupName, String groupDescription);
}
