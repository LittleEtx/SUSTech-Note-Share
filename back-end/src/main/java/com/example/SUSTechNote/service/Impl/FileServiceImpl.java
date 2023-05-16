package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.FileRepository;
import com.example.SUSTechNote.api.NoteRepository;
import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.exception.FileNotExistException;
import com.example.SUSTechNote.exception.ModifyNotAuthoredException;
import com.example.SUSTechNote.exception.NoteNotExistException;
import com.example.SUSTechNote.service.FileService;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileRepository fileRepository;

    private final StaticPathHelper staticPathHelper;
    private final NoteRepository noteRepository;
    private final NotebookService notebookService;

    public FileServiceImpl(FileRepository fileRepository, StaticPathHelper staticPathHelper,
                           NoteRepository noteRepository, NotebookService notebookService) {
        this.fileRepository = fileRepository;
        this.staticPathHelper = staticPathHelper;
        this.noteRepository = noteRepository;
        this.notebookService = notebookService;
    }

    // 需加入同步锁防止同时上传多个文件时出现文件ID重复的情况
    @Override
    public synchronized String uploadFile(String noteID, String fileName,
                             MultipartFile file, String fileID) throws IOException {
        checkAuthority(noteID);
        Note note = noteRepository.findNoteByNoteID(noteID);
        if (fileID == null || "".equals(fileID)) {
            var files = fileRepository.findFilesByNote_NoteID(noteID);
            int lastFileNum = files.stream()
                .map(f -> Integer.parseInt(f.getFileID().substring(f.getFileID().lastIndexOf("_") + 1)))
                .max(Integer::compareTo).orElse(0);
            // 新建文件，生成新的文件ID
            fileID = noteID + "_" + (lastFileNum + 1);
        }
        // 若笔记的文件夹不存在，则创建
        File noteFile = new File(staticPathHelper.getStaticPath(), note.getSavingPath());
        if (!noteFile.exists()) {
            logger.warn("note file not exist, create new one: " + noteFile.getAbsolutePath());
            if (!noteFile.mkdirs()) {
                logger.error("create note file failed: " + noteFile.getAbsolutePath());
                throw new IOException("create note file failed");
            }
        }
        String relativePath = note.getSavingPath()  + "/" + fileID;
        File targetFile = new File(noteFile, fileID);
        file.transferTo(targetFile); // this method will automatically delete file if already exist
        String fileUrl = "/api/static" + relativePath;

        fileRepository.save(new Files(fileID, fileName, fileUrl,
                relativePath, file.getContentType(), note));
        logger.debug("add file " + fileName + " to "
                + targetFile.getAbsolutePath() + " successfully, id=" + fileID);
        return fileID;
    }

    @Override
    public boolean deleteFile(String fileID){
        Files file = fileRepository.findFilesByFileID(fileID);
        if (file == null){
            throw new FileNotExistException("file not found");
        }
        Note note = file.getNote();
        checkAuthority(note);
        File filePath = new File(staticPathHelper.getStaticPath(), file.getSavingPath());
        if (!filePath.exists() || !filePath.isFile()) {
            logger.error("file not found, clean record in database");
            fileRepository.delete(file);
            return false;
        }
        if (filePath.delete()) {
            logger.info("file deleted successfully");
            fileRepository.delete(file);
            return true;
        } else {
            logger.error("file delete failed");
            return false;
        }
    }

    @Override
    public void renameFile(String noteID, String fileName, String newName){
        try {
            Note note = noteRepository.findNoteByNoteID(noteID);
            if (note != null){
                Files file = fileRepository.findFilesByNoteAndFileName(note,fileName);
                if (file != null){
                    file.setFileName(newName);
                    fileRepository.save(file);
                }else {
                    logger.error("file not found");
                }
            }else {
                logger.error("note not found");
            }
        }catch (Exception e) {
            logger.error("addNotebook error: " + e.getMessage());
        }
    }

    /**
     * Check whether the user has the authority to operate the note
     * @param noteID the note ID
     * @throws NoteNotExistException if the note does not exist
     * @throws ModifyNotAuthoredException if the user has no authority to modify the note
     */
    public void checkAuthority(String noteID)
            throws NoteNotExistException, ModifyNotAuthoredException {
        Note note = noteRepository.findNoteByNoteID(noteID);
        if (note == null) {
            throw new NoteNotExistException("note not found");
        }
        checkAuthority(note);
    }
    public void checkAuthority(Note note) {
        String notebookID = note.getNotebookID();
        if (!notebookService.checkAuthority(StpUtil.getLoginIdAsInt(), notebookID)) {
            throw new ModifyNotAuthoredException("user has no authority to modify this note");
        }
    }
}
