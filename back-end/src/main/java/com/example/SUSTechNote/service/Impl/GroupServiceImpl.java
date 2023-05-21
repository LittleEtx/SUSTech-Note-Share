package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.api.GroupRepository;
import com.example.SUSTechNote.api.NotebookRepository;
import com.example.SUSTechNote.api.UserRepository;
import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.interfaces.NotebookInterface;
import com.example.SUSTechNote.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final NotebookRepository notebookRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, NotebookRepository notebookRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.notebookRepository = notebookRepository;
    }

    @Override
    public List<JSONObject> loadJoinedGroup(int userID) {
        List<JSONObject> groups = new ArrayList<>();
        List<Integer> groupIDs = groupRepository.findGroupIDByUserID(userID);
        for (Integer groupID : groupIDs) {
            Group group = groupRepository.findById(groupID).get();
            JSONObject jsonObject = convertGroup2JsonObject(group);
            groups.add(jsonObject);
        }
        return groups;
    }

    JSONObject convertGroup2JsonObject(Group group) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupID", group.getGroupID());
        jsonObject.put("groupName", group.getGroupName());
        jsonObject.put("groupDescription", group.getGroupDescription());
        jsonObject.put("createTime", group.getCreateTime());
        jsonObject.put("groupOwnerName", group.getGroupOwnerName());
        jsonObject.put("groupOwnerID", group.getUser().getUserID());
        return jsonObject;
    }

    @Override
    public List<Group> loadJoinedGroup2(int userID) {
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
    public List<JSONObject> loadEnjoinedGroup(int userID) {
        List<Group> joinedGroups = loadJoinedGroup2(userID);
        List<Group> allGroups = groupRepository.findAll();
        List<Group> enjoinedGroups = new ArrayList<>();
        List<JSONObject> groups = new ArrayList<>();
        for (Group group : allGroups) {
            if (!joinedGroups.contains(group)) {
                enjoinedGroups.add(group);
            }
        }
        for (Group group : enjoinedGroups) {
            JSONObject jsonObject = convertGroup2JsonObject(group);
            groups.add(jsonObject);
        }
        return groups;
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
    public JSONObject groupInfo(int groupID) {
        Group group = groupRepository.findById(groupID).get();
        return convertGroup2JsonObject(group);
    }

    @Override
    public List<JSONObject> groupMembers(int groupID) {
        List<Integer> userIDs = groupRepository.findGroupMembers(groupID);
        List<JSONObject> users = new ArrayList<>();
        for (Integer userID : userIDs) {
            User user = userRepository.findUserByUserID(userID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", user.getUserID());
            jsonObject.put("userName", user.getUserName());
            jsonObject.put("avatar", user.getAvatar());
            users.add(jsonObject);
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

    @Override
    public List<JSONObject> searchGroupsWithLimit(String key,int pageNumber, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "groupid"));
        key = "%" + key + "%";
        int userID = StpUtil.getLoginIdAsInt();
        Page<JSONObject> groups = groupRepository.searchGroupsWithLimit(key,userID,pageRequest);
        List<JSONObject> groupsContent = groups.getContent();
        List<JSONObject> groupList = new ArrayList<>();
        for (JSONObject group : groupsContent) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("groupID", group.get("groupid"));
            jsonObject.put("groupName", group.get("group_name"));
            jsonObject.put("groupDescription", group.get("group_description"));
            jsonObject.put("createTime", group.get("create_time"));
            jsonObject.put("groupOwnerName", group.get("group_owner_name"));
            jsonObject.put("groupOwnerID", group.get("group_ownerid"));
            groupList.add(jsonObject);
        }
        return groupList;
    }
}
