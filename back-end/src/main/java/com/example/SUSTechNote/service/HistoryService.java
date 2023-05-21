package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.History;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryService {
    void createHistory(int userID, String notebookID);

    List<History> getHistory(int userID, int start, int limit);

    void deleteHistory(int userID, String notebookID);

    void deleteAllHistory(int userID);
}
