package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.api.CommentRepository;
import com.example.SUSTechNote.entity.Comment;
import com.example.SUSTechNote.entity.Reply;
import com.example.SUSTechNote.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public String comment(String notebookID, String comment, LocalDateTime commentTime) {
        int userID = StpUtil.getLoginIdAsInt();
        List<String> commentIDs = commentRepository.getCommentIDsByNotebookID(notebookID);
        String newCommentID;
        if (commentIDs.size() > 0) {
            Collections.sort(commentIDs);
            String lastCommentID = commentIDs.get(commentIDs.size() - 1);
            int lastCommentIDNum = Integer.parseInt(lastCommentID.substring(lastCommentID.lastIndexOf("_") + 1));
            newCommentID = notebookID + "_" + (lastCommentIDNum + 1);
        } else {
            newCommentID = notebookID + "_1";
        }
        try {
            commentRepository.createComment(newCommentID, comment, commentTime, notebookID, userID);
            return newCommentID;
        } catch (Exception e) {
            logger.error("create comment failed: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String deleteComment(String commentID) {
        int userID = StpUtil.getLoginIdAsInt();
        int commentUserID = commentRepository.getCommentUserID(commentID);
        int notebookUserID = commentRepository.getNotebookUserID(commentID);
        if (userID != commentUserID && userID != notebookUserID) {
            logger.error("delete comment failed: user " + userID + " has no authority to delete comment " + commentID);
            return "delete comment failed: user " + userID + " has no authority to delete comment " + commentID;
        } else {
            try {
                commentRepository.deleteRepliesUnderComment(commentID);
                commentRepository.deleteComment(commentID);
                return "delete comment " + commentID + " successfully";
            } catch (Exception e) {
                logger.error("delete comment failed: " + e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public List<JSONObject> getNotebookComments(String notebookID) {
        List<Comment> comments = commentRepository.getCommentsByNotebookID(notebookID);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Comment comment : comments) {
            JSONObject jsonObject = new JSONObject();
            List<Reply> replies = commentRepository.getRepliesByCommentID(comment.getCommentID());
            jsonObject.put("comment", comment);
            jsonObject.put("replies", replies);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }
}
