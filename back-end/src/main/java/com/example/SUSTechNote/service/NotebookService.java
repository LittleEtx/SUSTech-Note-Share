package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Notebook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotebookService {
    void addNotebook(String noteBookID, int userID, String directory, String savingPath, String title, String tag, String description, int isPublic);

    void updateNotebook(Notebook notebook);

    Boolean checkNotebook(String notebookID);

    void deleteNotebook(Integer status, String notebookID);

    Notebook getNotebookBasic(String notebookID);

    List<Notebook> findAllNotebook();

    Notebook findNotebookByID(String notebookID);

    int findNotebookCountByUserID(int userID);
}
