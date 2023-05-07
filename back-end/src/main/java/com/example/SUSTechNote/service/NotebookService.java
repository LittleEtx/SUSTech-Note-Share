package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Notebook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface NotebookService {
    void addNotebook(String noteBookID, int userID, String directory, String savingPath, String title, String tag, String description, int isPublic);

    void updateNotebook(Notebook notebook);

    Boolean checkNotebook(String notebookID);

    void deleteNotebook(Integer status, String notebookID);

    Notebook getNotebookBasic(String notebookID);

    List<Notebook> findNotebooks();

    Notebook findNotebookByID(String notebookID);

    int findNotebookCountByUserID(int userID);

    String uploadCover(String notebookID, MultipartFile cover) throws IOException;
}
