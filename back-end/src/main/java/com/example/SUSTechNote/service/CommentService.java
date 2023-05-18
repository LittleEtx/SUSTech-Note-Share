package com.example.SUSTechNote.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface CommentService {
    String comment(String notebookID, String comment, LocalDateTime commentTime);

    String deleteComment(String commentID);

    List<JSONObject> getNotebookComments(String notebookID);
}
