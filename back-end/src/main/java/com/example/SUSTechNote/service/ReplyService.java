package com.example.SUSTechNote.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface ReplyService {
    String deleteReply(String commentID, String reply);

    String reply(int userID, String commentID, String toUserName, String content, LocalDateTime replyTime);
}
