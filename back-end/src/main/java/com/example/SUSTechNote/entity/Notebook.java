package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notebooks")
public class Notebook {
    @Id
    private String notebookID;
    private String notebookName;
    private String tag;
    private LocalDateTime updateTime;
    private String cover;
    private String description;
    private Integer isPublic;
    private Integer likeNum;
    private Integer star;
    private Integer status;
    private LocalDateTime removeTime;

    private Integer authorID;

    public String getNotebookID() {
        return notebookID;
    }

    public void setNotebookID(String notebookID) {
        this.notebookID = notebookID;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }
    public Integer getUser() {
        return authorID;
    }

    public void setUser(Integer authorID) {
        this.authorID = authorID;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
}
