package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Notebook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public interface NotebookService {
    void addNotebook(String noteBookID, int userID, String directory, String savingPath, String title, String tag, String description, int isPublic);

    boolean updateNotebook(String notebookID, String notebookName, String tag, String description, String directory);

    String deleteNotebook(String notebookID);

    Notebook getNotebookBasic(String notebookID);

    List<Notebook> findNotebooks(int userID);

    Notebook findNotebookByID(String notebookID);

    int findNotebookCountByUserID(int userID);
    List<Notebook> findPublicNotebooks(int userID);

    String uploadCover(String notebookID, MultipartFile cover) throws IOException;


    List<String> findNotebookIDByUserID(int userID);

    List<Notebook> findSharedNotebooks(int userID);

    boolean shareToUser(String notebookID, int userID);

    boolean ifLike(String notebookID);

    boolean likeNotebook(String notebookID);

    String setNotebookPrivate(String notebookID);

    String setNotebookPublic(String notebookID);

    String shareToGroup(String notebookID, int groupID);

    String cancelUserShare(String notebookID, int userID);

    String cancelGroupShare(String notebookID, int groupID);
    List<Map<String,Object>> searchPublicNotebookWithLimit(String key, int limit);

    boolean findUserLikeExistByNotebookID(String notebookID);

    boolean findUserStarExistByNotebookID(String notebookID);

    void removeOneStarData(String notebookID);

    void removeOneLikeData(String notebookID);

    void StarNotebook(String notebookID);
}
