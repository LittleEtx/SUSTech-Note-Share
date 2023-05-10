package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.GroupRepository;
import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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
    public void updateGroup(int groupID, String groupName, String groupDescription) {
        Group group = groupRepository.findById(groupID).get();
        group.setGroupName(groupName);
        group.setGroupDescription(groupDescription);
        groupRepository.save(group);
    }
}
