package com.example.SUSTechNote.app;

import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.service.CourseService;
import com.example.SUSTechNote.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotebookApp {
    @Autowired
    NotebookService notebookService;

    @PostMapping("/addNotebook")
    public int addNotebook(Integer notebookID,String notebookName,Integer isPublic ){
        return notebookService.addNotebook(notebookID,notebookName,isPublic);
    };

    @PostMapping("/updateNotebook")
    public int updateNotebook(Notebook notebook){
        return notebookService.updateNotebook(notebook);
    };


    @GetMapping("/deleteNotebook")
    public int deleteNotebook(Integer notebookID){
        return notebookService.deleteNotebook(notebookID);
    };

    @GetMapping("/findAllNotebook")
    public List<Notebook> findAllNotebook(){
        return notebookService.findAllNotebook();
    };
}
