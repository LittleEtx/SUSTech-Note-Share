package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.GroupRepository;
import com.example.SUSTechNote.api.NotebookRepository;
import com.example.SUSTechNote.api.UserRepository;
import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.service.GroupService;
import com.example.SUSTechNote.interfaces.NotebookInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final NotebookRepository notebookRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, NotebookRepository notebookRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.notebookRepository = notebookRepository;
    }

    @Override
    public List<Group> loadJoinedGroup(int userID) {
        List<Group> groups = new ArrayList<>();
        List<Integer> groupIDs = groupRepository.findGroupIDByUserID(userID);
        for (Integer groupID : groupIDs) {
            Group group = groupRepository.findById(groupID).get();
            groups.add(group);
        }
        return groups;
    }

    @Override
    public String findUserNameByUserID(int userID) {
        return groupRepository.findUserNameByUserID(userID);
    }

    @Override
    public void createGroup(int userID, String groupName, String groupDescription, String createTime, String groupOwnerName) {
        Group group = new Group();
        User user = userRepository.findById(userID).get();
        group.setUser(user);
        group.setGroupName(groupName);
        group.setGroupDescription(groupDescription);
        group.setCreateTime(createTime);
        group.setGroupOwnerName(groupOwnerName);
        groupRepository.save(group);
        int groupID = group.getGroupID();
        groupRepository.addUserToGroup(userID, groupID);
    }

    @Override
    public List<Group> loadEnjoinedGroup(int userID) {
        List<Group> joinedGroups = loadJoinedGroup(userID);
        List<Group> allGroups = groupRepository.findAll();
        List<Group> enjoinedGroups = new ArrayList<>();
        for (Group group : allGroups) {
            if (!joinedGroups.contains(group)) {
                enjoinedGroups.add(group);
            }
        }
        return enjoinedGroups;
    }

    @Override
    public void joinGroup(int userID, int groupID) {
        groupRepository.addUserToGroup(userID, groupID);
    }

    @Override
    public void quitGroup(int userID, int groupID) {
        groupRepository.deleteUserFromGroup(userID, groupID);
    }

    @Override
    public void deleteGroup(int userID, int groupID) {
        groupRepository.deleteUsersInGroup(groupID);
        groupRepository.deleteGroup(userID, groupID);
    }

    @Override
    public String updateGroup(int userID, int groupID, String groupName, String groupDescription) {
        Group group = groupRepository.findById(groupID).get();
        if (group.getUser().getUserID() != userID) {
            return "User {} is not the owner of group {}".substring(userID, groupID);
        } else {
            group.setGroupName(groupName);
            group.setGroupDescription(groupDescription);
            groupRepository.save(group);
            return "Update group successfully";
        }
    }

    @Override
    public Group groupInfo(int groupID) {
        return groupRepository.findById(groupID).get();
    }

    @Override
    public List<User> groupMembers(int groupID) {
        List<Integer> userIDs = groupRepository.findGroupMembers(groupID);
        List<User> users = new ArrayList<>();
        for (Integer userID : userIDs) {
            User user = userRepository.findUserByUserID(userID);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<NotebookInterface> groupNotebookInfo(int groupID) {
        List<String> notebookIDs = groupRepository.findGroupNotebooksByGroupID(groupID);
        List<Notebook> notebooks = new ArrayList<>();
        for (String notebookID : notebookIDs) {
            Notebook notebook = notebookRepository.findNotebookByNotebookID(notebookID);
            notebooks.add(notebook);
        }
        return NotebookInterface.fromNotebooks(notebooks);
    }
}
