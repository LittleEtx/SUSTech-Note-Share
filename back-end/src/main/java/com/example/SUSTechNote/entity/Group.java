package com.example.SUSTechNote.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "my_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer GroupID;
    private String GroupName;
    private String GroupDescription;
    private Integer GroupOwnerID;
    private String GroupOwnerName;
    private String createTime;

    public Integer getGroupID() {
        return GroupID;
    }

    public void setGroupID(Integer groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupDescription() {
        return GroupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        GroupDescription = groupDescription;
    }

    public Integer getGroupOwnerID() {
        return GroupOwnerID;
    }

    public void setGroupOwnerID(Integer groupOwnerID) {
        GroupOwnerID = groupOwnerID;
    }

    public String getGroupOwnerName() {
        return GroupOwnerName;
    }

    public void setGroupOwnerName(String groupOwnerName) {
        GroupOwnerName = groupOwnerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
