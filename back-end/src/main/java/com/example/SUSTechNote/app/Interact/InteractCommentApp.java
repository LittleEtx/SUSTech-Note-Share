package com.example.SUSTechNote.app.Interact;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.service.CommentService;
import com.example.SUSTechNote.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController()
@RequestMapping("/interact/comments")

public class InteractCommentApp {

    private final CommentService commentService;
    private final ReplyService replyService;

    public InteractCommentApp(CommentService commentService, ReplyService replyService) {
        this.commentService = commentService;
        this.replyService = replyService;
    }

    /**
     * 获取笔记本的评论列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-notebook-comments")
    public ResponseEntity<?> getNotebookComments(@RequestParam("notebook") String notebookID){
        try {
            List<JSONObject> result = commentService.getNotebookComments(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 评论一个笔记本
     */
    @PostMapping("/comment")
    public ResponseEntity<?> comment(@RequestBody JSONObject jsonObject){
        String notebookID = jsonObject.getString("notebookID");
        String comment = jsonObject.getString("comment");
        String commentTimeStr = jsonObject.getString("commentTime");
        LocalDateTime commentTime = convertToLocalDateTime(commentTimeStr);
        try {
            String commentID = commentService.comment(notebookID, comment, commentTime);
            return ResponseEntity.ok(commentID);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除一个评论，该评论下的回复会被一并删除
     * @param commentID 评论ID
     */
    @DeleteMapping("/delete-comment")
    public ResponseEntity<?> deleteComment(@RequestParam("comment") String commentID){
        try {
            String result = commentService.deleteComment(commentID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 回复一个评论
     * @return 回复的楼层
     */
    @PostMapping("/reply")
    public ResponseEntity<?> reply(@RequestBody JSONObject jsonObject){
        int userID = StpUtil.getLoginIdAsInt();
        String commentID = jsonObject.getString("commentID");
        int floor = jsonObject.getInteger("reply");
        String content = jsonObject.getString("content");
        String replyTimeStr = jsonObject.getString("replyTime");
        LocalDateTime replyTime = convertToLocalDateTime(replyTimeStr);
        try {
            String replyID = replyService.reply(userID, commentID, floor, content, replyTime);
            return ResponseEntity.ok().body(replyID);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 删除一个回复
     * @param commentID 评论ID
     * @param reply 回复的楼层
     */
    @DeleteMapping("/delete-reply")
    public ResponseEntity<?> deleteReply(
            @RequestParam("comment") String commentID,
            @RequestParam("reply") String reply
    ){
        try {
            String result = replyService.deleteReply(commentID, reply);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public LocalDateTime convertToLocalDateTime(String timeStr){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(timeStr, dateTimeFormatter);
    }

}
