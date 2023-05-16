package com.example.SUSTechNote.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.api.HistoryRepository;
import com.example.SUSTechNote.api.NotebookRepository;
import com.example.SUSTechNote.entity.History;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HistoryRepository historyRepository;
    private final NotebookRepository notebookRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository, NotebookRepository notebookRepository) {
        this.historyRepository = historyRepository;
        this.notebookRepository = notebookRepository;
    }

    @Override
    public void createHistory(int userID, String notebookID, LocalDateTime visitTime) {
        try {
            historyRepository.createHistory(userID, notebookID, visitTime);
        } catch (Exception e) {
            logger.debug("create history failed");
            throw e;
        }
    }

    @Override
    public JSONObject getHistory(int userID) {
        JSONObject jsonObject = new JSONObject();
        List<History> histories = historyRepository.getHistory(userID);
        List<Notebook> notebooks = new ArrayList<>();
        for (History history : histories) {
            Notebook notebook = notebookRepository.findNotebookByNotebookID(history.getNotebookID());
            notebooks.add(notebook);
        }
        jsonObject.put("histories", histories);
        jsonObject.put("notebooks", notebooks);
        return jsonObject;
    }

    @Override
    public void deleteHistory(int userID, int historyID) {
        try {
            historyRepository.deleteHistory(userID, historyID);
        } catch (Exception e) {
            logger.debug("delete history failed");
            throw e;
        }
    }

    @Override
    public void deleteAllHistory(int userID) {
        try {
            historyRepository.deleteAllHistory(userID);
        } catch (Exception e) {
            logger.debug("delete all history failed");
            throw e;
        }
    }
}
