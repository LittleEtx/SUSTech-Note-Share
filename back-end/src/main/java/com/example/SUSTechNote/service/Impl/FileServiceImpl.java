package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.FileRepository;
import com.example.SUSTechNote.api.NoteRepository;
import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
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

    @Override
    public String uploadFile(String noteID, String fileName,
                             MultipartFile file, String fileID) throws IOException {
        checkAuthority(noteID);
        Note note = noteRepository.findNoteByNoteID(noteID);
        if (fileID == null || "".equals(fileID)) {
            // 新建文件，生成新的文件ID
            int count = fileRepository.findFileCountByNote(note.getNoteID());
            fileID = noteID + "_" + count;
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
        addFile(fileID,fileName,fileUrl,noteID);
        logger.debug("add file " + fileName + " to "
                + targetFile.getAbsolutePath() + " successfully, id=" + fileID);
        return fileID;
    }

    private void addFile(String fileID, String fileName, String fileUrl,String noteID){
        try {
            Note note = noteRepository.findNoteByNoteID(noteID);
            if (note != null){
                Files file = new Files();
                file.setFileUrl(fileUrl);
                file.setFileName(fileName);
                file.setFileID(fileID);
                file.setNote(note);
                fileRepository.save(file);
            }
        } catch (Exception e) {
            logger.error("addFile error: " + e.getMessage());
        }
    }
    @Override
    public void deleteFile(String noteID,String fileName){
        try {
            Note note = noteRepository.findNoteByNoteID(noteID);
            Files fileDatabase = fileRepository.findFilesByNoteAndFileName(note,fileName);
            if (checkFileExistByNoteAndFileInDatabase(note,fileDatabase)){
                File file = new File(fileDatabase.getFileUrl());
                if (file.delete()) {
                    logger.info("file deleted successfully");
                } else {
                    logger.error("file delete failed");
                }
            }
        } catch (Exception e) {
            logger.error("find note error: " + e.getMessage());
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


    public boolean checkFileExistByNoteAndFileInDatabase(Note note, Files fileDatabase) {
        if (note != null) {
            if (fileDatabase != null) {
                String fileUrl = fileDatabase.getFileUrl();
                File file = new File(fileUrl);
                if (file.exists()) {
                    return true;
                } else {
                    logger.error("file does not exist");
                    return false;
                }
            } else {
                logger.error("file not in database");
                return false;
            }
        } else {
            logger.error("note not found");
            return false;
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
        String notebookID = note.getNotebookID();
        if (!notebookService.checkAuthority(StpUtil.getLoginIdAsInt(), notebookID)) {
            throw new ModifyNotAuthoredException("user has no authority to modify this note");
        }
    }
}
