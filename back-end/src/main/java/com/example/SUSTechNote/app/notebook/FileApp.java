package com.example.SUSTechNote.app.notebook;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.exception.FileNotExistException;
import com.example.SUSTechNote.exception.ModifyNotAuthoredException;
import com.example.SUSTechNote.exception.NoteNotExistException;
import com.example.SUSTechNote.service.FileService;
import com.example.SUSTechNote.service.NotebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/notebook")
public class FileApp {
    private final Logger logger = LoggerFactory.getLogger(FileApp.class);

    private final FileService fileService;

    public FileApp(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("upload-file")
    public ResponseEntity<?> uploadFile(
            @RequestParam("note") String noteID,
            @RequestParam(name = "origin", defaultValue = "") String originFileID,
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String fileName
    ) {
        logger.debug("upload file: " + fileName + " to note: " + noteID);
        try {
            String id = fileService.uploadFile(noteID,fileName,file,originFileID);
            return ResponseEntity.ok(id);
        } catch (NoteNotExistException e) {
            return ResponseEntity.badRequest().body("Note not exist");
        } catch (ModifyNotAuthoredException e) {
            return ResponseEntity.badRequest().body("Not authorized to modify this note");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("File upload failed");
        }
    }

    @DeleteMapping("delete-file")
    public ResponseEntity<?> deleteFile(
            @RequestParam("file") String fileID
    ) {
        logger.debug("delete file: " + fileID);
        try {
            if (fileService.deleteFile(fileID)) {
                return ResponseEntity.ok("File deleted");
            } else {
                return ResponseEntity.badRequest().body("Failed to delete file!");
            }
        } catch (FileNotExistException e) {
            return ResponseEntity.badRequest().body("Note not exist");
        } catch (ModifyNotAuthoredException e) {
            return ResponseEntity.badRequest().body("Not authorized to modify this note");
        }
    }
}
