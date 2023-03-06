package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.NotebookRepository;

import com.example.SUSTechNote.entity.Notebook;

import com.example.SUSTechNote.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotebookServiceImpl implements NotebookService {
    @Autowired
    NotebookRepository notebookRepository;
    
    @Override
    public int addNotebook(Integer notebookID,String notebookName,Integer isPublic){
        if (checkNotebook(notebookID) == 0 ){
            Notebook notebook = new Notebook();
            notebook.setNotebookID(notebookID);
            notebook.setNotebookName(notebookName);
            notebook.setIsPublic(isPublic);
            notebookRepository.save(notebook);
            return 1;
        }
        if (checkNotebook(notebookID) == 400){
            return 400;
        }
        return 0;
    };

    @Override
    public int updateNotebook(Notebook notebook){
        if (checkNotebook(notebook.getNotebookID()) == 1 ){
            notebookRepository.save(notebook);
            return 1;
        }
        if (checkNotebook(notebook.getNotebookID()) == 400){
            return 400;
        }
        return 0;
    };

    @Override
    public int checkNotebook(Integer notebookID){
        List<Notebook> notebooks = notebookRepository.findNotebooksByNotebookID(notebookID);
        if (notebooks.size() == 1) {
            return 1;
        } else if (notebooks.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    };

    @Override
    public int deleteNotebook(Integer notebookID){
        if (checkNotebook(notebookID) == 1){
            notebookRepository.deleteById(notebookID);
            return 1;
        }
        return 0;
    };

    @Override
    public List<Notebook> findAllNotebook(){
        return notebookRepository.findAll();
    };
}
