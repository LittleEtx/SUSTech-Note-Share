package com.example.SUSTechNote.app.Interact;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.interfaces.GroupInterface;
import com.example.SUSTechNote.interfaces.UserInterface;
import com.example.SUSTechNote.service.Impl.AuthorityService;
import com.example.SUSTechNote.service.NotebookService;
import jdk.security.jarsigner.JarSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("interact/share")
public class InteractShareApp {
    private final Logger logger = LoggerFactory.getLogger(InteractShareApp.class);

    private final NotebookService notebookService;
    private final AuthorityService authorityService;

    public InteractShareApp(NotebookService notebookService,
                            AuthorityService authorityService) {
        this.notebookService = notebookService;
        this.authorityService = authorityService;
    }

    /**
     * 获取笔记本的分享用户列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-shared-users")
    public ResponseEntity<?> getShareUsers(@RequestParam("notebook") String notebookID){
        logger.info("getShareUsers: notebookID = {}", notebookID);
        Notebook notebook = authorityService.checkNotebookAuthority(notebookID);
        List<JSONObject> users = notebookService.getShareUsers(notebookID);
        return ResponseEntity.ok().body(users);
    }

    /**
     * 获取笔记本的分享群组列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-shared-groups")
    public ResponseEntity<?> getShareGroups(@RequestParam("notebook") String notebookID){
        Notebook notebook = authorityService.checkNotebookAuthority(notebookID);
//        var groups = GroupInterface.fromGroups(notebook.getGroups());
        List<JSONObject> groups = notebookService.getShareGroups(notebookID);
        return ResponseEntity.ok().body(groups);
    }

    /**
     * 将笔记本分享给一个用户
     * @param notebookID 笔记本ID
     * @param userID 用户ID
     */
    @PostMapping("/share-to-user")
    public ResponseEntity<?> shareToUser(
            @RequestParam("notebookID") String notebookID,
            @RequestParam("userID") int userID
    ){
        logger.info("shareToUser: notebookID = {}, userID = {}", notebookID, userID);
        if (notebookService.shareToUser(notebookID, userID)) {
            return ResponseEntity.ok().body("Share success");
        } else  {
            return ResponseEntity.badRequest().body("Share fail");
        }
    }

    /**
     * 将笔记本分享给一个群组
     */
    @PostMapping("/share-to-group")
    public ResponseEntity<?> shareToGroup(
            @RequestParam("notebookID") String notebookID,
            @RequestParam("groupID") int groupID
    ){
        try {
            logger.info("shareToGroup: notebookID = {}, groupID = {}", notebookID, groupID);
            String result = notebookService.shareToGroup(notebookID, groupID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Share fail. "+e.getMessage());
        }
    }

    /**
     * 取消将笔记本分享给用户
     */
    @PostMapping("/cancel-user-share")
    public ResponseEntity<?> cancelUserShare(
            @RequestParam("notebookID") String notebookID,
            @RequestParam("userID") int userID
    ){
        try {
            String result = notebookService.cancelUserShare(notebookID, userID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cancel fail. "+e.getMessage());
        }
    }

    /**
     * 取消将笔记本分享给群组
     */
    @PostMapping("/cancel-group-share")
    public ResponseEntity<?> cancelGroupShare(
            @RequestParam("notebookID") String notebookID,
            @RequestParam("groupID") int groupID
    ){
        try {
            String result = notebookService.cancelGroupShare(notebookID, groupID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cancel fail. "+e.getMessage());
        }
    }

}
