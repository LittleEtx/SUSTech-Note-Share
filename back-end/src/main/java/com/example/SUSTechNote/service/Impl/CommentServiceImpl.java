package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.api.CommentRepository;
import com.example.SUSTechNote.api.UserRepository;
import com.example.SUSTechNote.entity.Comment;
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
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
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
            List<Object[]> rows = commentRepository.getRepliesByCommentID(comment.getCommentID());
            JSONObject commentJson = convertComment(comment, rows);
            jsonObject.put("comment", commentJson);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

    public List<JSONObject> convertReplies2Json(List<Object[]> replies) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Object[] reply : replies) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("replyID", reply[0]);
            jsonObject.put("replyContent", reply[1]);
            jsonObject.put("replyTime", reply[2]);
            jsonObject.put("toUserName", reply[3]);
            jsonObject.put("commentID", reply[4]);
            jsonObject.put("userID", reply[5]);
            jsonObject.put("userName", userRepository.findUserByUserID((int) reply[5]).getUserName());
            jsonObject.put("userAvatar", userRepository.findUserByUserID((int) reply[5]).getAvatar());
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

    public JSONObject convertComment(Comment comment, List<Object[]> replies) {
        JSONObject commentJson = new JSONObject();
        commentJson.put("commentID", comment.getCommentID());
        commentJson.put("commentContent", comment.getCommentContent());
        commentJson.put("notebookID", comment.getNotebook().getNotebookID());
        commentJson.put("userID", comment.getUser().getUserID());
        commentJson.put("commentTime", comment.getCommentTime());
        commentJson.put("replyNum", replies.size());
        commentJson.put("replies", convertReplies2Json(replies));
        return commentJson;
    }
}
