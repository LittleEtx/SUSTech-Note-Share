package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.interfaces.NotebookInterface;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/center")
public class CenterApp {
    private final Logger logger = LoggerFactory.getLogger(CenterApp.class);
    private final NotebookService notebookService;
    private final UserService userService;

    public CenterApp(NotebookService notebookService, UserService userService) {
        this.notebookService = notebookService;
        this.userService = userService;
    }

    @GetMapping("get-notebooks")
    public ResponseEntity<?> findNotebooks(){
        try {
            int userID = StpUtil.getLoginIdAsInt();
            List<Notebook> notebooks = notebookService.findNotebooks(userID);
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    @GetMapping("public-notebooks")
    public ResponseEntity<?> findNotebookByNotebookID(@RequestParam("user") int userID){
        try {
            List<Notebook> notebooks = notebookService.findPublicNotebooks(userID);
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    @GetMapping("get-starred-notebooks")
    public ResponseEntity<?> findStarredNotebooks(){
        User user = userService.findUserById(StpUtil.getLoginIdAsInt());
        try {
            List<Notebook> starredNotebooks = user.getStarNotebookList();
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(starredNotebooks));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    @GetMapping("get-shared-notebooks")
    public ResponseEntity<?> findSharedNotebooks(){
        int userID = StpUtil.getLoginIdAsInt();
        try {
            List<Notebook> notebooks = notebookService.findSharedNotebooks(userID);
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }
}
