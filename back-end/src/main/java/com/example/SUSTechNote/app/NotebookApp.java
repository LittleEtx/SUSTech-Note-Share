package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.service.CourseService;
import com.example.SUSTechNote.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotebookApp {
    @Autowired
    NotebookService notebookService;

    @PostMapping("/notebook/basic")
    public Notebook getNotebookBasic(@RequestBody JSONObject jsonObject){
        String notebookID = jsonObject.getString("notebookID");
        return notebookService.getNotebookBasic(notebookID);
    };

    @PostMapping("/addNotebook")
    public int addNotebook(String notebookID,String notebookName,Integer isPublic ){
        return notebookService.addNotebook(notebookID,notebookName,isPublic);
    };

    @PostMapping("/updateNotebook")
    public int updateNotebook(Notebook notebook){
        return notebookService.updateNotebook(notebook);
    };

    @SaCheckLogin
    @GetMapping("/deleteNotebook")
    public int deleteNotebook(String notebookID){
        Notebook notebook = notebookService.findNotebookByID(notebookID);
        if (StpUtil.getLoginIdAsInt() == notebook.getAuthorID()){
            return notebookService.deleteNotebook(notebookID);
        }else {
            return 0;
        }
    };

    @GetMapping("/findAllNotebook")
    public List<Notebook> findAllNotebook(){
        return notebookService.findAllNotebook();
    };


}
