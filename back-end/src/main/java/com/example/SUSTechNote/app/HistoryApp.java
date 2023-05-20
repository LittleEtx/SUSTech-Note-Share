package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.StpUtil;
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

    @PostMapping("/createHistory")
    public ResponseEntity<?> createHistory(@RequestBody JSONObject jsonObject) {
        int userID = Integer.parseInt(jsonObject.getString("userID"));
        String notebookID = jsonObject.getString("notebookID");
        LocalDateTime visitTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String visitTimeStr = visitTime.format(dateTimeFormatter);
        visitTime = LocalDateTime.parse(visitTimeStr, dateTimeFormatter);
        try {
            historyService.createHistory(userID, notebookID, visitTime);
            return ResponseEntity.ok().body("create history success");
        } catch (Exception e) {
            logger.debug("create history failed");
            return ResponseEntity.badRequest().body("create history failed");
        }
    }

    @GetMapping("/getHistory")
    public ResponseEntity<?> getHistory() {
        int userID = StpUtil.getLoginIdAsInt();
        try {
            return ResponseEntity.ok().body(historyService.getHistory(userID));
        } catch (Exception e) {
            logger.debug("get history failed");
            return ResponseEntity.badRequest().body("get history failed\n"+e.getMessage());
        }
    }

    @PostMapping("/deleteHistory")
    public ResponseEntity<?> deleteHistory(@RequestBody JSONObject jsonObject) {
        int userID = StpUtil.getLoginIdAsInt();
        int historyID = Integer.parseInt(jsonObject.getString("historyID"));
        try {
            historyService.deleteHistory(userID, historyID);
            return ResponseEntity.ok().body("delete history success");
        } catch (Exception e) {
            logger.debug("delete history failed");
            return ResponseEntity.badRequest().body("delete history failed");
        }
    }

    @PostMapping("/deleteAllHistory")
    public ResponseEntity<?> deleteAllHistory() {
        int userID = StpUtil.getLoginIdAsInt();
        try {
            historyService.deleteAllHistory(userID);
            return ResponseEntity.ok().body("delete all history success");
        } catch (Exception e) {
            logger.debug("delete all history failed");
            return ResponseEntity.badRequest().body("delete all history failed");
        }
    }
}
