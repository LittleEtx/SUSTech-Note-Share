package com.example.SUSTechNote.app.Interact;

import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.service.NotebookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/interact/share")
public class InteractShareApp {

    private final NotebookService notebookService;

    public InteractShareApp(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    /**
     * 获取笔记本的分享用户列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-shared-users")
    public ResponseEntity<?> getShareUsers(@RequestParam("notebook") String notebookID){
        try {
            Notebook notebook = notebookService.findNotebookByID(notebookID);
            return ResponseEntity.ok().body(notebook.getUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 获取笔记本的分享群组列表
     * @param notebookID 笔记本ID
     */
    @GetMapping("/get-shared-groups")
    public ResponseEntity<?> getShareGroups(@RequestParam("notebook") String notebookID){
        try {
            Notebook notebook = notebookService.findNotebookByID(notebookID);
            List<Group> groups = notebook.getGroups();
            for (Group group : groups) {
                group.setUser(null);
                group.setNotebookList(null);
            }
            return ResponseEntity.ok().body(groups);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 将笔记本分享给一个用户
     * @param notebookID 笔记本ID
     * @param userID 用户ID
     */
    @PostMapping("/share-to-user")
    public ResponseEntity<?> shareToUser(@RequestParam("notebook") String notebookID, @RequestParam("target") String userID){
        try {
            String result = notebookService.shareToUser(notebookID, userID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Share fail"+e.getMessage());
        }
    }

    /**
     * 将笔记本分享给一个群组
     */
    @PostMapping("/share-to-group")
    public ResponseEntity<?> shareToGroup(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") int groupID
    ){
        try {
            String result = notebookService.shareToGroup(notebookID, groupID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Share fail"+e.getMessage());
        }
    }

    /**
     * 取消将笔记本分享给用户
     */
    @PostMapping("/cancel-user-share")
    public ResponseEntity<?> cancelUserShare(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") int userID
    ){
        try {
            String result = notebookService.cancelUserShare(notebookID, userID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cancel fail"+e.getMessage());
        }
    }

    /**
     * 取消将笔记本分享给群组
     */
    @PostMapping("/cancel-group-share")
    public ResponseEntity<?> cancelGroupShare(
            @RequestParam("notebook") String notebookID,
            @RequestParam("target") int groupID
    ){
        try {
            String result = notebookService.cancelGroupShare(notebookID, groupID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cancel fail"+e.getMessage());
        }
    }

}
