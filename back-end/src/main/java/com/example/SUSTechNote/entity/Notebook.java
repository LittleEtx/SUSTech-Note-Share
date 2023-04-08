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

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }
    public String getNotebookID() {
        return notebookID;
    }

    public void setNotebookID(String notebookID) {
        this.notebookID = notebookID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(LocalDateTime removeTime) {
        this.removeTime = removeTime;
    }

    public Integer getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Integer authorID) {
        this.authorID = authorID;
    }

//    public String getNotebookID() {
//        return notebookID;
//    }
//
//    public void setNotebookID(String notebookID) {
//        this.notebookID = notebookID;
//    }
//
//    public String getNotebookName() {
//        return notebookName;
//    }
//
//    public void setNotebookName(String notebookName) {
//        this.notebookName = notebookName;
//    }
//    public Integer getUser() {
//        return authorID;
//    }
//
//    public void setUser(Integer authorID) {
//        this.authorID = authorID;
//    }
//
//    public Integer getIsPublic() {
//        return isPublic;
//    }
//
//    public void setIsPublic(Integer isPublic) {
//        this.isPublic = isPublic;
//    }
}
