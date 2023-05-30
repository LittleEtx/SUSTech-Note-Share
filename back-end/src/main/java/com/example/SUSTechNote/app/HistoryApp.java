package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/history")
public class HistoryApp {
    private final Logger logger = LoggerFactory.getLogger(HistoryApp.class);
    private final HistoryService historyService;

    public HistoryApp(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/visit")
    public ResponseEntity<?> createHistory(
            @RequestParam("notebook") String notebookID
    ) {
        int userID = StpUtil.getLoginIdAsInt();
        logger.debug("User {} visit notebook {}", userID, notebookID);
        try {
            historyService.createHistory(userID, notebookID);
            return ResponseEntity.ok().body("create history success");
        } catch (Exception e) {
            logger.debug("create history failed");
            return ResponseEntity.badRequest().body("create history failed");
        }
    }

    @GetMapping("/get-notebook-history")
    public ResponseEntity<?> getHistory(
            @RequestParam(name = "start", required = false, defaultValue = "0") Integer start,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        int userID = StpUtil.getLoginIdAsInt();
        logger.debug("User {} get history, start = {}, limit = {}", userID, start, limit);
        try {
            var historyList = historyService.getHistory(userID, start, limit);
            JSONArray jsonArray = new JSONArray();
            for (var history : historyList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("notebookID", history.getNotebookID());
                jsonObject.put("visitTime", history.getVisitTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                jsonArray.add(jsonObject);
            }
            return ResponseEntity.ok().body(jsonArray);
        } catch (Exception e) {
            logger.debug("get history failed");
            return ResponseEntity.badRequest().body("get history failed\n"+e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHistory(
            @RequestParam("notebook") String notebookID
    ) {
        int userID = StpUtil.getLoginIdAsInt();
        logger.debug("User {} delete history of notebook {}", userID, notebookID);
        try {
            historyService.deleteHistory(userID, notebookID);
            return ResponseEntity.ok().body("delete history success");
        } catch (Exception e) {
            logger.debug("delete history failed");
            return ResponseEntity.badRequest().body("delete history failed");
        }
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllHistory() {
        int userID = StpUtil.getLoginIdAsInt();
        logger.debug("User {} delete all history", userID);
        try {
            historyService.deleteAllHistory(userID);
            return ResponseEntity.ok().body("delete all history success");
        } catch (Exception e) {
            logger.debug("delete all history failed");
            return ResponseEntity.badRequest().body("delete all history failed");
        }
    }
}
