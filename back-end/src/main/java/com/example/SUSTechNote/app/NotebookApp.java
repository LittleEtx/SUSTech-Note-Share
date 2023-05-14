package com.example.SUSTechNote.app;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.interfaces.NotebookInterface;
import com.example.SUSTechNote.service.NoteService;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import io.lettuce.core.dynamic.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/notebook")
public class NotebookApp {

    private final Logger logger = LoggerFactory.getLogger(NotebookApp.class);
    private final NotebookService notebookService;
    private final NoteService noteService;
    private final StaticPathHelper staticPathHelper;

    public NotebookApp(NotebookService notebookService, NoteService noteService, StaticPathHelper staticPathHelper) {
        this.notebookService = notebookService;
        this.noteService = noteService;
        this.staticPathHelper = staticPathHelper;
    }

    /**
     * 创建笔记本
     * @return 笔记本ID
     */
    @PostMapping("/create")
    public ResponseEntity<?> createNotebook(@RequestBody JSONObject jsonObject){
        //获取目前登录用户的id
        int userID = StpUtil.getLoginIdAsInt();
        int count = notebookService.findNotebookCountByUserID(userID);
        if(count >= 500){
            return ResponseEntity.badRequest().body("You have reached the maximum number of notebooks");
        }
        String basePath = staticPathHelper.getStaticPath() + "/notebooks/" + userID + "/";
        List<String> notebookIDs = notebookService.findNotebookIDByUserID(userID);
        Collections.sort(notebookIDs);
        String lastNotebookID = notebookIDs.get(notebookIDs.size() - 1);
        int lastNotebookNum = Integer.parseInt(lastNotebookID.substring(lastNotebookID.lastIndexOf("_") + 1));
        String noteBookID = userID + "_" + (lastNotebookNum + 1);
        String savePath = basePath + noteBookID;
        File folder = new File(savePath);
        if (!folder.exists()) {
            logger.info("create noteBook folder: " + folder.getAbsolutePath());
            if (!folder.mkdirs()) {
                logger.warn("create notebook folder failed!");
                return ResponseEntity.internalServerError()
                        .body("Notebook creation failed");
            }
        }
        String directory = jsonObject.getString("directory");
        String realPath = savePath.substring(savePath.lastIndexOf("static"));
        String title = jsonObject.getString("title");
        String tag = jsonObject.getString("tag");
        String description = jsonObject.getString("description");
        Boolean isPublic = jsonObject.getBoolean("isPublic");
        if (title == null || directory == null || isPublic == null) {
            return ResponseEntity.badRequest()
                    .body("Must provide title, directory and isPublic");
        }
        if (title.isEmpty() || directory.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Title and directory cannot be empty");
        }
        try {
            notebookService.addNotebook(noteBookID, userID,
                    directory, realPath, title, tag, description, isPublic ? 1 : 0);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Notebook creation failed");
        }
        logger.info("user " + userID + " created notebook " + noteBookID);
        return ResponseEntity.ok(noteBookID);
    }

    /**
     * 获取笔记本的基本信息
     * @param notebookID 笔记本ID
     * @return 笔记本基本信息
     */
    @GetMapping("/basic")
    public ResponseEntity<?> getNotebookBasic(@Param("notebookID") String notebookID){
        var notebook = notebookService.getNotebookBasic(notebookID);
        if (notebook == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(NotebookInterface.fromNotebook(notebook));
        }
    }

    @PostMapping("/upload_cover")
    public ResponseEntity<?> uploadCover(@RequestParam("cover")MultipartFile cover, @RequestParam("notebookID") String notebookID){
        if (checkAuthority(notebookID)) {
            try {
                String url = notebookService.uploadCover(notebookID, cover);
                return ResponseEntity.ok("Cover uploaded successfully \n" + url);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Cover upload failed \n" + e);
            }
        } else {
            return ResponseEntity.badRequest().body("You have no authority to upload cover");
        }
    }

    @PostMapping("/update_info")
    public ResponseEntity<?> updateNotebook(@RequestBody JSONObject jsonObject){
        String notebookID = jsonObject.getString("notebookID");
        String title = jsonObject.getString("title");
        String tag = jsonObject.getString("tag");
        String description = jsonObject.getString("description");
        if (!checkAuthority(notebookID)) {
            return ResponseEntity.badRequest().body("You have no authority to update this notebook");
        }
        try {
            notebookService.updateNotebook(notebookID, title, tag, description);
            return ResponseEntity.ok("Notebook updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Notebook update failed" + e);
        }
    }

    @PostMapping("/rename_dir")
    public ResponseEntity<?> renameDir(@RequestBody JSONObject jsonObject) {
        String oldName = jsonObject.getString("old_name");
        String newName = jsonObject.getString("new_name");
        int userID = StpUtil.getLoginIdAsInt(); //获取用户ID
        try {
            notebookService.renameDir(userID, oldName, newName);
            return ResponseEntity.ok("Directory renamed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Directory rename failed" + e);
        }
    }

    @SaCheckLogin
    @PostMapping("/delete")
    public ResponseEntity<?> deleteNotebook(@RequestBody JSONObject jsonObject){
        String notebookID = jsonObject.getString("notebookID");
        Notebook notebook = notebookService.findNotebookByID(notebookID);
        if (StpUtil.getLoginIdAsInt() == notebook.getAuthorID()){
            try {
                String result = notebookService.deleteNotebook(notebookID);
                return ResponseEntity.ok(result);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Notebook deletion failed");
            }
        }else {
            return ResponseEntity.badRequest().body("You are not the author of this notebook");
        }
    }

    @PostMapping("/directory")
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
            List<JSONObject> directories = traversal(file, prePath);
            return ResponseEntity.ok(directories);
        }
    }

    public List<JSONObject> traversal(File file, String prePath) {
        List<JSONObject> directories = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null){
                for (File f : files){
                    if (f.isDirectory()){
                        String noteID = f.getName();
                        String noteName = noteService.getNoteNameByNoteID(noteID);
                        jsonObject.put("directory", noteName);
                        jsonObject.put("id", noteID);
                        jsonObject.put("files", traversal(f, prePath+f.getName()+"/"));
                        directories.add(jsonObject);
                    } else {
                        jsonObject.put("file", f.getName());
                        jsonObject.put("url", prePath+f.getName());
                        directories.add(jsonObject);
                    }
                }
            }
        }
        return directories;
    }

    public Boolean checkAuthority(String notebookID) {
        int userID = StpUtil.getLoginIdAsInt(); //获取用户ID
        return notebookService.checkAuthority(userID, notebookID);
    }
}
