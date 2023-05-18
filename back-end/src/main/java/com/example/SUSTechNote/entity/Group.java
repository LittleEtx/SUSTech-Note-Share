package com.example.SUSTechNote.entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "my_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer GroupID;
    private String GroupName;
    private String GroupDescription;
    @ManyToOne
    @JoinColumn(name = "group_ownerid")
    private User user;
    private String GroupOwnerName;
    private String createTime;

    @ManyToMany
    @JoinTable(
            name = "group_notebook",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "notebook_id")
    )
    List<Notebook> notebookList;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Notebook> getNotebookList() {
        return notebookList;
    }

    public void setNotebookList(List<Notebook> notebookList) {
        this.notebookList = notebookList;
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
