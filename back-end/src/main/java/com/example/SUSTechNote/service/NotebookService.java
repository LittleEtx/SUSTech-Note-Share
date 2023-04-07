package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Course;
import com.example.SUSTechNote.entity.Notebook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotebookService {
    public int addNotebook(String notebookID,String notebookName,Integer isPublic );

    public int updateNotebook(Notebook notebook);

    public int checkNotebook(String notebookID);

    public int deleteNotebook(String notebookID);

    public Notebook getNotebookBasic(String notebookID);

    public List<Notebook> findAllNotebook();
}
