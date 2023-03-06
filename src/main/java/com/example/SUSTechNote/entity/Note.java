package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Note {
    @Id
    private Integer noteID;
    private String content;

    private Integer isPublic;

    /**
     * 多对一关系，由多方维系关系
     * 通过note表的notebook_id字段和Notebook表的id主键字段做关系映射
     */
    @ManyToOne
    @JoinColumn(name = "notebook_id")
    private Notebook notebook;

    @ManyToOne
    @JoinColumn(name = "fav_user_note")
    private User fav_user_note;

    public Note() {
    }

    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }
}
