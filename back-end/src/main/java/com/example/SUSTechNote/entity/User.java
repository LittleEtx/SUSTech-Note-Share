package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Integer userID;
    private String userName;
    private String password;
    private String email;
//    课表和头像以图片格式储存？string存文件路径
    private String schedule;
    private String avatar;

    private String permission;



    private String description;

    private Integer gender = -1;

    private Date birth;

    private LocalDateTime updateTime;

    /**
     * OneToMany和ManyToOne配合使用时，由ManyToOne多方进行关系管理
     * 此时只需要指定管理映射属性，为Notebook中的user属性
     */
    @OneToMany(mappedBy = "user")
    private List<Notebook> notebookList;

    /**
     * 多对多，通过JoinTable生成第三方表，指定各自主键的存放列名
     * joinColumns：将本表id，存储到第三方表，列名为per_id
     * inverseJoinColumns：将对方表id，存储到第三方表，列名为dept_id
     * 注意：此处存放到第三方表的列名，需要和对方的外键名称相一致
     */
    @ManyToMany
    @JoinTable(
            name = "user_fav_notebook",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "notebook_id")
    )
    private List<Notebook> favNotebookList;

    @ManyToMany
    @JoinTable(
            name = "user_fav_note",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private List<Note> favNoteList;

    @ManyToMany
    @JoinTable(
            name = "user_course",    // 自动生成的第三方表名，可省略
            joinColumns = @JoinColumn(name = "user_id"),       // 将本表id，存储到第三方表，列名为per_id
            inverseJoinColumns = @JoinColumn(name = "course_id")       // 将对方表id，存储到第三方表，列名为dept_id
    )
    private List<Course> courseList;




    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    };

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
