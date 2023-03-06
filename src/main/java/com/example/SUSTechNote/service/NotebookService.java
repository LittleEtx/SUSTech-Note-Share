package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Course;
import com.example.SUSTechNote.entity.Notebook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotebookService {
    public int addNotebook(Integer notebookID,String notebookName,Integer isPublic );

    public int updateNotebook(Notebook notebook);

    public int checkNotebook(Integer notebookID);

    public int deleteNotebook(Integer notebookID);

    public List<Notebook> findAllNotebook();
}
