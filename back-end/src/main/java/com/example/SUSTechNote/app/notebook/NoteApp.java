package com.example.SUSTechNote.app.notebook;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.NoteService;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/notebook")
public class NoteApp {
    private final Logger logger = LoggerFactory.getLogger(NoteApp.class);
    private final NoteService noteService;
    private final NotebookService notebookService;
    private final StaticPathHelper staticPathHelper;

    public NoteApp(NoteService noteService, NotebookService notebookService, StaticPathHelper staticPathHelper) {
        this.noteService = noteService;
        this.notebookService = notebookService;
        this.staticPathHelper = staticPathHelper;
    }


    @PostMapping("/create_note")
    public ResponseEntity<?> createNote(
            @RequestParam("notebook") String notebookID,
            @RequestParam("name") String name,
            @RequestParam(name = "public", defaultValue = "true") Boolean isPublic
    ) {
        int count = noteService.findNotesCountByNotebookID(notebookID);
        if (count >= 100) {
            return ResponseEntity.badRequest().body("You have reached the maximum number of notes in this notebook");
        }
        //获取目前登录用户的id
        int userID = StpUtil.getLoginIdAsInt();
        if (!notebookService.checkAuthority(userID, notebookID)) {
            return ResponseEntity.badRequest().body("You are not authored to modify the notebook!");
        }
        List<String> noteIDs = noteService.findNoteIDsByNotebookID(notebookID);
        int lastNoteNum = noteIDs.stream()
                .map(id -> Integer.parseInt(id.substring(id.lastIndexOf("_") + 1)))
                .max(Integer::compareTo).orElse(0);
        String noteID = notebookID + "_" + (lastNoteNum + 1);
        String relativePath = "/notebooks/" + userID + "/" + notebookID + "/" + noteID;
        String savePath = staticPathHelper.getStaticPath() + relativePath;
        File folder = new File(savePath);
        if (!folder.exists()) {
            logger.info("create note folder: " + folder.getAbsolutePath());
            if (!folder.mkdirs()) {
                logger.error("create note folder failed");
                return ResponseEntity.badRequest().body("Note creation failed");
            }
        }
        try {
            noteService.addNote(noteID, userID, notebookID, relativePath, name, isPublic ? 1 : 0);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Note creation failed");
        }
        return ResponseEntity.ok(noteID);

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
