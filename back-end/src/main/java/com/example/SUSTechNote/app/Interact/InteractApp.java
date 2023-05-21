package com.example.SUSTechNote.app.Interact;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.SUSTechNote.service.NotebookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interact")
public class InteractApp {

    private final NotebookService notebookService;

    public InteractApp(NotebookService notebookService) {
        this.notebookService = notebookService;
    }


    /**
     * 获取用户最近的点赞列表
     */
    @GetMapping("/if-like")
    public ResponseEntity<?> getIfLike(
            @RequestParam("notebook") String notebookID
    ){
        try {
            boolean result = notebookService.ifLike(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 点赞一个笔记
     */
    @PostMapping("/like-notebook")
    public ResponseEntity<?> likeNotebook(
            @RequestParam("notebook") String notebookID
    ){
        try {
            boolean result = notebookService.likeNotebook(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("false\n"+e.getMessage());
        }
    }

    /**
     * 取消点赞一个笔记
     * @param notebookID 笔记本ID
     */
    @SaCheckLogin
    @PostMapping("/cancel-like-notebook")
    public ResponseEntity<?> cancelLikeNotebook(
            @RequestParam("notebook") String notebookID
    ){
        //TODO
        if (notebookService.findUserLikeExistByNotebookID(notebookID)){
            notebookService.removeOneLikeData(notebookID);
            return ResponseEntity.ok("cancel like successfully");
        }
        return ResponseEntity.ok("cancel like failed");
    }

    /**
     * 获取用户是否收藏了一个笔记
     * @param notebookID 笔记本ID
     */
    @SaCheckLogin
    @GetMapping("/if-star")
    public ResponseEntity<?> getIfStar(
            @RequestParam("notebook") String notebookID
    ){
        // TODO
        if (notebookService.findUserStarExistByNotebookID(notebookID)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    /**
     * 收藏一个笔记
     * @param notebookID 笔记本ID
     */
    @SaCheckLogin
    @PostMapping("/star-notebook")
    public ResponseEntity<?> starNotebook(
            @RequestParam("notebook") String notebookID
    ){
        // TODO
        if (notebookService.findUserStarExistByNotebookID(notebookID)){
            return ResponseEntity.badRequest().body("user has stared the notebook");
        }
        notebookService.StarNotebook(notebookID);
        return ResponseEntity.ok("star the notebook successfully");
    }

    /**
     * 取消收藏一个笔记
     * @param notebookID 笔记本ID
     */
    @PostMapping("/cancel-star-notebook")
    public ResponseEntity<?> cancelStarNotebook(
            @RequestParam("notebook") String notebookID
    ){
        //TODO
        if (notebookService.findUserStarExistByNotebookID(notebookID)){
            notebookService.removeOneStarData(notebookID);
            return ResponseEntity.ok("cancel star successfully");
        }
        return ResponseEntity.badRequest().body("cancel star failed");
    }

    /**
     * 公开一个笔记
     */
    @PostMapping("/set-notebook-public")
    public ResponseEntity<?> setNotebookPublic(
            @RequestParam("notebook") String notebookID
    ){
        try {
            String result = notebookService.setNotebookPublic(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("setNotebookPublic error: "+e.getMessage());
        }
    }

    /**
     * 将一个笔记设为私有。执行该操作后：
     * <li> 笔记本的点赞数将被清空 </li>
     * <li> 所有人对该笔记本的收藏都将被移除，收藏数归零 </li>
     * <li> 所有该笔记本下的评论都将被清除 </li>
     */
    @PostMapping("/set-notebook-private")
    public ResponseEntity<?> setNotebookPrivate(
            @RequestParam("notebook") String notebookID
    ){
        try {
            String result = notebookService.setNotebookPrivate(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fail\n"+e.getMessage());
        }
    }


}
