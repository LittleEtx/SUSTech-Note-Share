package com.example.SUSTechNote.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/interact/share")
public class InteractShareApp {

    /**
     * 获取笔记本的分享用户列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-shared-users")
    public ResponseEntity<?> getShareUsers(
            @RequestParam("notebook") String notebookID
    ){
        //TODO
        return null;
    }

    /**
     * 获取笔记本的分享群组列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-shared-groups")
    public ResponseEntity<?> getShareGroups(
            @RequestParam("notebook") String notebookID
    ){
        //TODO
        return null;
    }

    /**
     * 将笔记本分享给一个用户
     * @param notebookID 笔记本ID
     * @param userID 用户ID
     */
    @PostMapping("/share-to-user")
    public ResponseEntity<?> shareToUser(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") String userID
    ){
        //TODO
        return null;
    }

    /**
     * 将笔记本分享给一个群组
     */
    @PostMapping("/share-to-group")
    public ResponseEntity<?> shareToGroup(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") String groupID
    ){
        //TODO
        return null;
    }

    /**
     * 取消将笔记本分享给用户
     */
    @PostMapping("/cancel-user-share")
    public ResponseEntity<?> cancelUserShare(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") String userID
    ){
        //TODO
        return null;
    }

    /**
     * 取消将笔记本分享给群组
     */
    @PostMapping("/cancel-group-share")
    public ResponseEntity<?> cancelGroupShare(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") String groupID
    ){
        //TODO
        return null;
    }


}
