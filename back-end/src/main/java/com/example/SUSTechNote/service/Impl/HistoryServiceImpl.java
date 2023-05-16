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
    public List<JSONObject> getHistory(int userID) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        List<History> histories = historyRepository.getHistory(userID);
        for (History history : histories) {
            LocalDateTime visitTime = history.getVisitTime();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("historyID", history.getHistoryID());
            jsonObject.put("notebookID", history.getNotebookID());
            jsonObject.put("visitTime", history.getVisitTime());
            jsonObject.put("userID", history.getUser().getUserID());
            Notebook notebook = notebookRepository.findNotebookByNotebookID(history.getNotebookID());
            jsonObject.put("notebookName", notebook.getNotebookName());
            jsonObject.put("notebookDescription", notebook.getDescription());
            jsonObject.put("authorID", notebook.getAuthorID());
            jsonObject.put("cover", notebook.getCover());
            jsonObject.put("directory", notebook.getDirectory());
            jsonObject.put("isPublic", notebook.getIsPublic());
            jsonObject.put("likeNum", notebook.getLikeNum());
            jsonObject.put("star", notebook.getStar());
            jsonObject.put("tag", notebook.getTag());
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
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
