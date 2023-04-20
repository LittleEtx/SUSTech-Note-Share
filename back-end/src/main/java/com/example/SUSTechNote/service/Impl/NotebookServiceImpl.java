package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.NotebookRepository;

import com.example.SUSTechNote.entity.Notebook;

import com.example.SUSTechNote.service.NotebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class NotebookServiceImpl implements NotebookService {

    private final Logger logger = LoggerFactory.getLogger(NotebookServiceImpl.class);
    private final NotebookRepository notebookRepository;

    public NotebookServiceImpl(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }
    
    @Override
    public void addNotebook(String noteBookID, int userID, String directory, String savingPath, String title, String tag, String description, int isPublic){
        try {
            LocalDateTime now = LocalDateTime.now();
            Notebook notebook = new Notebook();
            notebook.setNotebookID(noteBookID);
            notebook.setAuthorID(userID);
            notebook.setDirectory(directory);
            notebook.setSavePath(savingPath);
            notebook.setNotebookName(title);
            notebook.setTag(tag);
            notebook.setDescription(description);
            notebook.setIsPublic(isPublic);
            notebook.setUpdateTime(now);
            notebook.setLikeNum(0);
            notebook.setStar(0);
            notebook.setStatus(0);
            notebookRepository.save(notebook);
        } catch (Exception e) {
            logger.error("addNotebook error: " + e.getMessage());
        }
    }

    @Override
    public void updateNotebook(Notebook notebook){
        try {
            notebookRepository.save(notebook);
        } catch (Exception e) {
            logger.error("updateNotebook error: " + e.getMessage());
        }
    }

    @Override
    public Boolean checkNotebook(String notebookID){
        List<Notebook> notebooks = notebookRepository.findNotebooksByNotebookID(notebookID);
        if (notebooks.size() == 1) {
            Notebook notebook = notebooks.get(0);
            if (notebook.getStatus() != 0) {
                logger.error("checkNotebook error: " + notebookID + "'s status is not 0.");
                return false;
            }
            return true;
        } else if (notebooks.size() > 1) {
            logger.error("checkNotebook error: " + notebookID + " has more than one record.");
            return false;
        } else {
            logger.error("checkNotebook error: " + notebookID + " does not exist.");
            return false;
        }
    }

    @Override
    public void deleteNotebook(Integer status, String notebookID){
        if (checkNotebook(notebookID)){
            notebookRepository.changeStatusByNotebookID(status, notebookID);
        }
    }

    @Override
    public Notebook getNotebookBasic(String notebookID) {
        return notebookRepository.findNotebooksByNotebookID(notebookID).get(0);
    }


    @Override
    public List<Notebook> findAllNotebook(){
        return notebookRepository.findAll();
    }

    @Override
    public Notebook findNotebookByID(String notebookID){
        return notebookRepository.findNotebooksByNotebookID(notebookID).get(0);
    }

    @Override
    public int findNotebookCountByUserID(int userID) {
        return notebookRepository.findNotebookCountByAuthorID(userID);
    }
}
