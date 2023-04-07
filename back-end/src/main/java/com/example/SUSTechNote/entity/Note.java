package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    private Integer noteID;
    private String noteName;
    private Integer authorID;
    private Integer isPublic;
    private Integer likeNum;
    private Integer star;
    private Integer status;
    private LocalDateTime removeTime;
    private String notebookID;

    public Note() {
    }

    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }


    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public Integer getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Integer authorID) {
        this.authorID = authorID;
    }

    public Integer getLike() {
        return likeNum;
    }

    public void setLike(Integer like) {
        this.likeNum = like;
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

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public String getNotebookID() {
        return notebookID;
    }

    public void setNotebookID(String notebookID) {
        this.notebookID = notebookID;
    }
}
