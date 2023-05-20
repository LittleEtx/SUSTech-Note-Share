package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.ReplyRepository;
import com.example.SUSTechNote.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    private final Logger logger = LoggerFactory.getLogger(ReplyServiceImpl.class);
    private final ReplyRepository replyRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public String deleteReply(String commentID, String reply) {
        int userID = StpUtil.getLoginIdAsInt();
        int ReplyUserID = replyRepository.getReplyUserID(commentID, reply);
        int notebookUserID = replyRepository.getNotebookUserID(commentID);
        if (userID != ReplyUserID && userID != notebookUserID) {
            logger.error("delete reply failed: user " + userID + " has no authority to delete reply " + reply);
            return "delete reply failed: user " + userID + " has no authority to delete reply " + reply;
        } else {
            try {
                replyRepository.deleteReply(commentID, reply);
                return "delete reply " + reply + " successfully";
            } catch (Exception e) {
                logger.error("delete reply failed: " + e.getMessage());
                return "delete reply failed: " + e.getMessage();
            }
        }
    }

    @Override
    public String reply(int userID, String commentID, String toUserName, String content, LocalDateTime replyTime) {
        // 0表示回复楼主，否则表示回复楼中楼
        String newReplyID;
        List<String> replyIDs = replyRepository.getReplyIDsByCommentID(commentID);
        if (replyIDs.size() > 0) {
            String lastReplyID = replyIDs.get(replyIDs.size() - 1);
            int lastReplyIDNum = Integer.parseInt(lastReplyID.substring(lastReplyID.lastIndexOf("_") + 1));
            newReplyID = commentID + "_" + (lastReplyIDNum + 1);
        } else {
            newReplyID = commentID + "_1";
        }
        try {
            replyRepository.createReply(newReplyID, toUserName, content, replyTime, commentID, userID);
            return newReplyID;
        } catch (Exception e) {
            logger.error("create reply failed: " + e.getMessage());
            throw e;
        }
    }
}
