package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.NoteService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collections;
import java.util.List;

@RestController
public class NoteApp {
    private final Logger logger = LoggerFactory.getLogger(NoteApp.class);
    private final NoteService noteService;
    private final StaticPathHelper staticPathHelper;

    public NoteApp(NoteService noteService, StaticPathHelper staticPathHelper) {
        this.noteService = noteService;
        this.staticPathHelper = staticPathHelper;
    }


    @PostMapping("/notebook/new_directory")
    public ResponseEntity<?> createNote(@RequestBody JSONObject jsonObject) {
        String noteBookID = jsonObject.getString("notebookID");
        int count = noteService.findNotesCountByNotebookID(noteBookID);
        if (count >= 500) {
            return ResponseEntity.badRequest().body("You have reached the maximum number of notes in this notebook");
        } else {
            String title = jsonObject.getString("title");
            int isPublic = jsonObject.getInteger("isPublic");
            //获取目前登录用户的id
            int userID = StpUtil.getLoginIdAsInt();
            List<String> noteIDs = noteService.findNoteIDsByNotebookID(noteBookID);
            Collections.sort(noteIDs);
            String lastNoteID = noteIDs.get(noteIDs.size() - 1);
            int lastNoteNum = Integer.parseInt(lastNoteID.substring(lastNoteID.lastIndexOf("_") + 1));
            String noteID = noteBookID + "_" + (lastNoteNum + 1);
            String basePath = staticPathHelper.getStaticPath() + "/notebooks/" + userID + "/" + noteBookID + "/";
            String savePath = basePath + noteID;
            String realPath = savePath.substring(savePath.lastIndexOf("static"));
            File folder = new File(savePath);
            if (!folder.exists()) {
                logger.info("create note folder: " + folder.getAbsolutePath());
                folder.mkdirs();
            }
            try {
                noteService.addNote(noteID, userID, noteBookID, realPath, title, isPublic);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Note creation failed");
            }
            return ResponseEntity.ok(noteID);
        }
    }

    @PostMapping("/updateNote")
    public int updateNote(Note note) {
        return noteService.updateNote(note);
    }


    @PostMapping ("/note/delete")
    public ResponseEntity<?> deleteNote(@RequestBody JSONObject jsonObject) {
        String noteID = jsonObject.getString("noteID");
        try {
            String result = noteService.deleteNote(noteID);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Note deletion failed \n" + e);
        }
    }

    @GetMapping("/findAllNote")
    public List<Note> findAllNote() {
        return noteService.findAllNote();
    }
}
