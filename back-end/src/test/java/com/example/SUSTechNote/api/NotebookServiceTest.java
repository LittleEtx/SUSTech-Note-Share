package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Notebook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class NotebookServiceTest {
    @Autowired
    NotebookRepository repository;

    @Test
    void getNotebooks() {
        // get notebook repo
        List<Notebook> notebooks = repository.findNotebookByAuthorID(12112628);
        System.out.println(notebooks.size());
        for (Notebook notebook : notebooks) {
            System.out.println(notebook.getNotebookName());
        }
    }

    @Test
    void createNotebook() {
        // create notebook
        Notebook notebook = new Notebook();
        notebook.setNotebookID("12112628_13");
        notebook.setNotebookName("test");
        notebook.setUpdateTime(LocalDateTime.now());
        notebook.setAuthorID(12112628);
        notebook.setNotebookName("test");
        notebook.setDirectory("test");
        notebook.setIsPublic(1);
        notebook.setCover("test");
        notebook.setTag("test");
        notebook.setDescription("test");
        repository.save(notebook);
    }
}
