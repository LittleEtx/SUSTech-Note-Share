package com.example.SUSTechNote.app.Interact;

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
    public ResponseEntity<?> getIfLike(@RequestParam String notebookID){
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
            @RequestParam("id") String notebookID
    ){
        try {
            boolean result = notebookService.likeNotebook(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("false\n"+e.getMessage());
        }
    }

    /**
     * 公开一个笔记
     */
    @PostMapping("/set-notebook-public")
    public ResponseEntity<?> setNotebookPublic(
            @RequestParam("id") String notebookID
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
            @RequestParam("id") String notebookID
    ){
        try {
            String result = notebookService.setNotebookPrivate(notebookID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fail\n"+e.getMessage());
        }
    }


}
