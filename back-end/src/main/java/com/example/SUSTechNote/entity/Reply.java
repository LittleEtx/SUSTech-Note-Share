package com.example.SUSTechNote.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
public class Reply {
    @Id
    private String replyID;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
    private String toUserName;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "longtext")
    private String replyContent;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime replyTime;

    public String getReplyID() {
        return replyID;
    }

    public void setReplyID(String replyID) {
        this.replyID = replyID;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
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
