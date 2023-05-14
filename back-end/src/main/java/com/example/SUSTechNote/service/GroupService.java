package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.interfaces.NotebookInterface;
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

    String updateGroup(int userID, int groupID, String groupName, String groupDescription);

    Group groupInfo(int groupID);

    List<User> groupMembers(int groupID);

    List<NotebookInterface> groupNotebookInfo(int groupID);
}
