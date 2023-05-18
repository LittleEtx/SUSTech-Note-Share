package com.example.SUSTechNote.app;

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

    public SearchApp(UserService userService,NotebookService notebookService) {
        this.userService = userService;
        this.notebookService = notebookService;
    }

    @GetMapping("user")
    public ResponseEntity<?> searchUser(@RequestParam("key") String key){
        List<Map<String,Object>> users = userService.searchUsersWithLimit(key,10);
        if (users.size()>0){
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.ok("no user found");
        }
    }

    @GetMapping("notebook")
    public ResponseEntity<?> searchNotebook(@RequestParam("key") String key){
        //搜索公开的笔记本
        List<Map<String,Object>> notebooks = notebookService.searchPublicNotebookWithLimit(key,10);
        if (notebooks.size()>0){
            return ResponseEntity.ok(notebooks);
        } else {
            return ResponseEntity.ok("no notebook found");
        }
    }
}
