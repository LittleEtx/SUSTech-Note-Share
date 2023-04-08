package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    private String courseID;
    private String courseName;

    /**
     * 通过mappedBy表明由Person实体的department属性进行关系管理
     */
    @ManyToMany(mappedBy = "courseList")
    private List<User> userList;

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
