package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
public class NotebookApp {

    private final Logger logger = LoggerFactory.getLogger(NotebookApp.class);
    private final NotebookService notebookService;
    private final StaticPathHelper staticPathHelper;

    public NotebookApp(NotebookService notebookService, StaticPathHelper staticPathHelper) {
        this.notebookService = notebookService;
        this.staticPathHelper = staticPathHelper;
    }

    @PostMapping("center/notebooks/create")
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
            int is_public = jsonObject.getInteger("is_public");
            try {
                notebookService.addNotebook(noteBookID,userID,directory,realPath,title,tag,description,is_public);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Notebook creation failed");
            }
            return ResponseEntity.ok(noteBookID);
        }
    }

    @PostMapping("/notebook/basic")
    public Notebook getNotebookBasic(@RequestBody JSONObject jsonObject){
        String notebookID = jsonObject.getString("notebookID");
        return notebookService.getNotebookBasic(notebookID);
    }

    @PostMapping("/notebook/upload_cover")
    public ResponseEntity<?> uploadCover(@RequestParam("cover")MultipartFile cover, @RequestParam("notebookID") String notebookID){
        try {
            logger.debug("try upload cover");
            String url = notebookService.uploadCover(notebookID, cover);
            logger.debug("success upload cover");
            return ResponseEntity.ok("Cover uploaded successfully \n" + url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cover upload failed \n" + e);
        }
    }

    @PostMapping("/updateNotebook")
    public ResponseEntity<?> updateNotebook(Notebook notebook){
        try {
            notebookService.updateNotebook(notebook);
            return ResponseEntity.ok("Notebook updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Notebook update failed");
        }
    }

    @SaCheckLogin
    @PostMapping("notebook/delete")
    public ResponseEntity<?> deleteNotebook(@RequestBody JSONObject jsonObject){
        int status = jsonObject.getInteger("status");
        String notebookID = jsonObject.getString("notebookID");
        System.out.println(notebookID);
        Notebook notebook = notebookService.findNotebookByID(notebookID);
        if (StpUtil.getLoginIdAsInt() == notebook.getAuthorID()){
            try {
                notebookService.deleteNotebook(status, notebookID);
                return ResponseEntity.ok("Notebook deleted successfully");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Notebook deletion failed");
            }
        }else {
            return ResponseEntity.badRequest().body("You are not the author of this notebook");
        }
    }

    @GetMapping("/center/notebooks/get")
    public ResponseEntity<?> findNotebooks(){
        try {
            List<Notebook> notebooks = notebookService.findNotebooks();
            return ResponseEntity.ok(notebooks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Notebook query failed \n" + e);
        }
    }

    @PostMapping("/notebook/directory")
    public ResponseEntity<?> getNotebookDirectory(@RequestBody JSONObject jsonObject){
        String notebookID = jsonObject.getString("notebookID");
        Notebook notebook = notebookService.findNotebookByID(notebookID);
        if (notebook == null){
            return ResponseEntity.badRequest().body("Notebook does not exist");
        } else {
            String subPath = notebook.getSavePath().substring(notebook.getSavePath().lastIndexOf("/notebooks"));
            String basePath = staticPathHelper.getStaticPath();
            String directory = basePath + subPath;
            String prePath = directory.substring(directory.lastIndexOf("static"))+"/";
            File file = new File(directory);
            JSONObject jsonObject1 = traversal(file, new JSONObject(), prePath);
            return ResponseEntity.ok(jsonObject1);
        }
    }

    public JSONObject traversal(File file, JSONObject jsonObject, String prePath) {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null){
                for (File f : files){
                    if (f.isDirectory()){
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject.put(f.getName(), traversal(f, jsonObject1, prePath+f.getName()+"/"));
                    } else {
                        jsonObject.put(f.getName(), prePath+f.getName());
                    }
                }
            }
        }
        return jsonObject;
    }
}
