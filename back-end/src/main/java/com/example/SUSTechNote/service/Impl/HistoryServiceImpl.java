package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.HistoryRepository;
import com.example.SUSTechNote.entity.History;
import com.example.SUSTechNote.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public void createHistory(int userID, String notebookID) {
        try {
            historyRepository.deleteHistory(userID, notebookID); // 删除原有的历史记录
            historyRepository.createHistory(userID, notebookID, LocalDateTime.now());
        } catch (Exception e) {
            logger.debug("create history failed");
            throw e;
        }
    }

    @Override
    public List<History> getHistory(int userID, int start, int limit) {
        List<History> histories = historyRepository.getHistory(userID); // 默认按时间降序排列
        if (start < 0 || start >= histories.size()) {
            return List.of();
        }
        return histories.subList(start, Math.min(start + limit, histories.size()));
    }

    @Override
    public void deleteHistory(int userID, String notebookID) {
        try {
            historyRepository.deleteHistory(userID, notebookID);
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
