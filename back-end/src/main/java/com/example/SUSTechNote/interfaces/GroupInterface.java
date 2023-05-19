package com.example.SUSTechNote.interfaces;

import com.example.SUSTechNote.entity.Group;

import java.util.Collection;
import java.util.List;

public record GroupInterface(
    int GroupID,
    String groupName,
    String groupDescription,
    UserInterface owner,
    String createTime
) {
    public static GroupInterface fromGroup(Group group) {
        return new GroupInterface(
            group.getGroupID(),
            group.getGroupName(),
            group.getGroupDescription(),
            UserInterface.fromUser(group.getUser()),
            group.getCreateTime()
        );
    }
    public static List<GroupInterface> fromGroups(Collection<Group> groups) {
        return groups.stream().map(GroupInterface::fromGroup).toList();
    }
}
