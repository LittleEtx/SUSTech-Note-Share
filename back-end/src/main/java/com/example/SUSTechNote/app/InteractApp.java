package com.example.SUSTechNote.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/interact")
public class InteractApp {

    /**
     * 获取用户最近的点赞列表
     */
    @GetMapping("/get-like-list")
    public ResponseEntity<?> getLikeList(){
        //TODO
        return null;
    }

    /**
     * 点赞一个笔记
     */
    @PostMapping("/like-notebook")
    public ResponseEntity<?> likeNotebook(
            @RequestParam("id") String notebookID
    ){
        //TODO
        return null;
    }

    /**
     * 公开一个笔记
     */
    @PostMapping("/set-notebook-public")
    public ResponseEntity<?> setNotebookPublic(
            @RequestParam("id") String notebookID
    ){
        //TODO
        return null;
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
        //TODO
        return null;
    }


}
