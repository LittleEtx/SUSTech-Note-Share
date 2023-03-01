package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "notebooks")
public class Notebook {
    @Id
    private Integer notebookID;
    private String notebookName;

    private Integer isPublic;

    /**
     * 多对一关系，由多方维系关系
     * 通过course表的course_id字段和department表的id主键字段做关系映射
     */
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fav_user_notebook")
    private User fav_user_notebook;

    /**
     * OneToMany和ManyToOne配合使用时，由ManyToOne多方进行关系管理
     * 此时只需要指定管理映射属性，为note中的notebook属性
     */
    @OneToMany(mappedBy = "notebook")
    private List<Note> noteList;

    public Integer getNotebookID() {
        return notebookID;
    }

    public void setNotebookID(Integer notebookID) {
        this.notebookID = notebookID;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
}
