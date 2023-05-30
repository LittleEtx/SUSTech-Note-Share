package com.example.SUSTechNote.service;

import com.example.SUSTechNote.api.NotebookRepository;
import com.example.SUSTechNote.service.Impl.HistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HistoryServiceTest {
    @Autowired
    HistoryServiceImpl service;
    @Test
    void createHistory() {
        service.createHistory(12112628, "12112628_13");
    }

    @Test
    void getHistory() {
        var result = service.getHistory(12112628, 0, 10);
        result.forEach(System.out::println);
    }
}
