package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.HistoryRepository;
import com.example.SUSTechNote.service.HistoryService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HistoryRepository historyRepository;
    private final StaticPathHelper staticPathHelper;

    public HistoryServiceImpl(HistoryRepository historyRepository,
                              StaticPathHelper staticPathHelper) {
        this.historyRepository = historyRepository;
        this.staticPathHelper = staticPathHelper;
    }

}
