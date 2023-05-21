package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.interfaces.UserInterface;
import com.example.SUSTechNote.service.GroupService;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchApp {
    private final Logger logger = LoggerFactory.getLogger(SearchApp.class);
    private final UserService userService;
    private final NotebookService notebookService;
    private final GroupService groupService;

    public SearchApp(UserService userService,NotebookService notebookService,GroupService groupService) {
        this.userService = userService;
        this.notebookService = notebookService;
        this.groupService = groupService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> searchUser(@RequestParam("key") String key){
        logger.info("searchUser: key = {}", key);
        List<Map<String,Object>> users = userService.searchUsersWithLimit(key,10);
        if (users.size() > 0){
            return ResponseEntity.ok(UserInterface.fromUserMap(users));
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/notebook")
    public ResponseEntity<?> searchNotebook(@RequestParam("key") String key){
        //搜索公开的笔记本
        List<Map<String,Object>> notebooks = notebookService.searchPublicNotebookWithLimit(key,10);
        if (notebooks.size()>0){
            return ResponseEntity.ok(notebooks);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @SaCheckLogin
    @GetMapping("/group")
    public ResponseEntity<?> searchGroup(@RequestParam("key") String key){
        //按照ID或者群组名字来搜索用户已经加入的群组，返回匹配的群组的信息列表
        List<JSONObject> groups = groupService.searchGroupsWithLimit(key,10);
        if (groups.size()>0){
            return ResponseEntity.ok(groups);
        } else {
            return ResponseEntity.badRequest().body("No group found");
        }
    }
}
