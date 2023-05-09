package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/center")
public class CenterApp {
    private final Logger logger = LoggerFactory.getLogger(CenterApp.class);
    private final NotebookService notebookService;
    private final StaticPathHelper staticPathHelper;

    public CenterApp(NotebookService notebookService, StaticPathHelper staticPathHelper) {
        this.notebookService = notebookService;
        this.staticPathHelper = staticPathHelper;
    }

    @PostMapping("notebooks/create")
    public ResponseEntity<?> createNotebook(@RequestBody JSONObject jsonObject){
        //获取目前登录用户的id
        int userID = StpUtil.getLoginIdAsInt();
        int count = notebookService.findNotebookCountByUserID(userID);
        if(count >= 500){
            return ResponseEntity.badRequest().body("You have reached the maximum number of notebooks");
        } else {
            String basePath = staticPathHelper.getStaticPath() + "/notebooks/" + userID + "/";
            String noteBookID = userID + "_" + (count + 1);
            String savePath = basePath + noteBookID;
            File folder = new File(savePath);
            if (!folder.exists()) {
                logger.info("create noteBook folder: " + folder.getAbsolutePath());
                folder.mkdirs();
            }
            String directory = jsonObject.getString("directory");
            String realPath = savePath.substring(savePath.lastIndexOf("static"));
            String title = jsonObject.getString("title");
            String tag = jsonObject.getString("tag");
            String description = jsonObject.getString("description");
            int is_public = jsonObject.getInteger("isPublic");
            try {
                notebookService.addNotebook(noteBookID,userID,directory,realPath,title,tag,description,is_public);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Notebook creation failed");
            }
            return ResponseEntity.ok(noteBookID);
        }
    }

    @GetMapping("notebooks/get")
    public ResponseEntity<?> findNotebooks(){
        try {
            List<Notebook> notebooks = notebookService.findNotebooks();
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    @GetMapping("public-notebooks")
    public ResponseEntity<?> findNotebookByNotebookID(@Param("userID") int userID){
        try {
            List<Notebook> notebooks = notebookService.findPublicNotebooks(userID);
            return ResponseEntity.ok(NotebookInterface.fromNotebooks(notebooks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    private  record NotebookInterface(
        String notebookID,
        int authorID,
        String title,
        String tag,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updateTime,
        String cover,
        String description,
        String directory,
        int isPublic,
        int likeCount,
        int starCount

    ){
        public static NotebookInterface fromNotebook(Notebook notebook){
            return new NotebookInterface(
                notebook.getNotebookID(),
                notebook.getAuthorID(),
                notebook.getNotebookName(),
                notebook.getTag(),
                notebook.getUpdateTime(),
                notebook.getCover(),
                notebook.getDescription(),
                notebook.getDirectory(),
                notebook.getIsPublic(),
                notebook.getLikeNum() == null ? 0 : notebook.getLikeNum(),
                notebook.getStar() == null ? 0 : notebook.getStar()
            );
        }
        public static List<NotebookInterface> fromNotebooks(List<Notebook> notebooks){
            return notebooks.stream().map(NotebookInterface::fromNotebook).toList();
        }
    }
}
