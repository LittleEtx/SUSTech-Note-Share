package com.example.SUSTechNote.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface HistoryService {
    void createHistory(int userID, String notebookID, LocalDateTime visitTime);

    List<JSONObject> getHistory(int userID);

    void deleteHistory(int userID, int historyID);

    void deleteAllHistory(int userID);
}
