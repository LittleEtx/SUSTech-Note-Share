package com.example.SUSTechNote.app.notebook;

import com.example.SUSTechNote.exception.ModifyNotAuthoredException;
import com.example.SUSTechNote.service.FileService;
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
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String fileName
    ) {
        logger.debug("upload file: " + fileName + " to note: " + noteID);
        try {
            String id = fileService.uploadFile(noteID, fileName, file);
            return ResponseEntity.ok(id);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("File upload failed");
        }
    }

    /**
     * 更新一个已有文件
     */
    @PostMapping("update-file")
    public ResponseEntity<?> updateFile(
            @RequestParam("id") String fileID,
            @RequestParam("file") MultipartFile file
    ) {
        logger.debug("update file: " + fileID);
        if (fileService.updateFile(file, fileID)) {
            return ResponseEntity.ok("File updated");
        } else {
            return ResponseEntity.badRequest().body("Failed to update file!");
        }
    }

    @DeleteMapping("delete-file")
    public ResponseEntity<?> deleteFile(
            @RequestParam("file") String fileID
    ) {
        logger.debug("delete file: " + fileID);
        if (fileService.deleteFile(fileID)) {
            return ResponseEntity.ok("File deleted");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete file!");
        }
    }

    @PostMapping("move-file")
    public ResponseEntity<?> moveFile(
            @RequestParam("file") String fileID,
            @RequestParam("note") String noteID
    ){
        logger.debug("move file: " + fileID + " to note: " + noteID);
        if (fileService.moveFile(fileID,noteID)){
            return ResponseEntity.ok("File moved");
        }else {
            return ResponseEntity.badRequest().body("Failed to move file!");
        }
    }

    @PostMapping("rename-file")
    public ResponseEntity<?> renameFile(
            @RequestParam("file") String fileID,
            @RequestParam("name") String newName
    ){
        logger.debug("rename file: " + fileID + " to new name: " + newName);
        try {
            fileService.renameFile(fileID,newName);
            return ResponseEntity.ok("File renamed");
        }catch (ModifyNotAuthoredException e) {
            return ResponseEntity.badRequest().body("Not authorized to modify this file");
        }
    }
}
