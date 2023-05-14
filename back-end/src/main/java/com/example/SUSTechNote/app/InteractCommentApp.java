package com.example.SUSTechNote.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/interact/comment")
public class InteractCommentApp {

    /**
     * 获取笔记本的评论列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-notebook-comments")
    public ResponseEntity<?> getNotebookComments(
            @RequestParam("notebook") String notebookID
    ){
        //TODO
        return null;
    }

    /**
     * 评论一个笔记本
     * @param notebookID 笔记本ID
     * @param comment 评论内容
     * @return 评论ID
     */
    @PostMapping("/comment")
    public ResponseEntity<?> comment(
            @RequestParam("notebook") String notebookID,
            @RequestBody String comment
    ){
        //TODO
        return null;
    }

    /**
     * 删除一个评论，该评论下的回复会被一并删除
     * @param commentID 评论ID
     */
    @DeleteMapping("/delete-comment")
    public ResponseEntity<?> deleteComment(
            @RequestParam("comment") String commentID
    ){
        //TODO
        return null;
    }

    /**
     * 回复一个评论
     * @param commentID 评论ID
     * @param reply 回复的楼层，0表示回复评论，1表示回复第一个回复，以此类推
     * @param content 回复内容
     * @return 回复的楼层
     */
    @PostMapping("/reply")
    public ResponseEntity<?> reply(
            @RequestParam("comment") String commentID,
            @RequestParam("reply") Integer reply,
            @RequestBody String content
    ){
        //TODO
        return null;
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
        //TODO
        return null;
    }

}
