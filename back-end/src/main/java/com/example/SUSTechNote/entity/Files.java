package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class Files {
    @Id
    private String fileID;
    private String fileName;
    private String fileUrl;
    private String savingPath;
    private Integer status;
    private LocalDateTime removeTime;

    /**
     * 多对一关系，由多方维系关系
     * 通过note表的notebook_id字段和Notebook表的id主键字段做关系映射
     */
    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    public Files(String fileID, String fileName, String fileUrl, String savingPath, Note note) {
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.savingPath = savingPath;
        this.note = note;
    }

    public Files() {
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getSavingPath() {
        return savingPath;
    }
    public void setSavingPath(String savingPath) {
        this.savingPath = savingPath;
    }
}
