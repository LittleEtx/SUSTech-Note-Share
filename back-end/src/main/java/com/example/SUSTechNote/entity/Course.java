package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    private Integer courseID;
    private String courseName;

    /**
     * OneToMany和ManyToOne配合使用时，由ManyToOne多方进行关系管理
     * 此时只需要指定管理映射属性，为Notebook中的course属性
     */
    @OneToMany(mappedBy = "course")
    private List<Notebook> notebookList;

    /**
     * 通过mappedBy表明由Person实体的department属性进行关系管理
     */
    @ManyToMany(mappedBy = "courseList")
    private List<User> userList;

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
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
