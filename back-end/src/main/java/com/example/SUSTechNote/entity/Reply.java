package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer replyID;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    private int floor;
    private String replyContent;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime replyTime;

    public Integer getReplyID() {
        return replyID;
    }

    public void setReplyID(Integer replyID) {
        this.replyID = replyID;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(LocalDateTime replyTime) {
        this.replyTime = replyTime;
    }
}
