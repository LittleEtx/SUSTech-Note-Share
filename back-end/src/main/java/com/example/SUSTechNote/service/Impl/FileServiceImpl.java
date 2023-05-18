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
                             MultipartFile file) throws IOException {
        Note note = checkNoteAuthority(noteID);
        var notebookID = note.getNotebookID();
        var files = fileRepository.findFilesByNotebook(notebookID);
        int lastFileNum = files.stream()
            .map(f -> Integer.parseInt(f.getFileID().substring(f.getFileID().lastIndexOf("_") + 1)))
            .max(Integer::compareTo).orElse(0);
        // 新建文件，生成新的文件ID
        String fileID = notebookID + "_" + (lastFileNum + 1);

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
    public boolean updateFile(MultipartFile file, String fileID) {
        var fileInfo = checkFileAuthority(fileID);
        var targetFile = new File(staticPathHelper.getStaticPath(), fileInfo.getSavingPath());
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            logger.warn("update file failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteFile(String fileID){
        Files file = checkFileAuthority(fileID);
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
    public void renameFile(String fileID, String newName){
        Files file = checkFileAuthority(fileID);
        file.setFileName(newName);
        fileRepository.save(file);
    }


    // 需加入同步锁防止同时移动多个文件时出现文件ID重复的情况
    @Override
    public synchronized boolean moveFile(String fileID, String noteID){
        Files file = checkFileAuthority(fileID);
        Note note = checkNoteAuthority(noteID);
        var relativeSavingPath = note.getSavingPath() + "/" + fileID;
        if (relativeSavingPath.equals(file.getSavingPath())) {
            logger.trace("move file to itself");
            return true;
        }

        //判断目标位置是否已存在同名文件
        File originFile = new File(staticPathHelper.getStaticPath(), file.getSavingPath());
        File destinationFile = new File(staticPathHelper.getStaticPath(), relativeSavingPath);
        if (destinationFile.exists()){
            logger.error("file existed in target note");
            return false;
        }
        System.out.println();
        logger.debug("rename file " + originFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
        if(originFile.renameTo(destinationFile)){
            //更新数据库
            file.setFileUrl("/api/static" + relativeSavingPath);
            file.setNote(note);
            file.setSavingPath(relativeSavingPath);
            fileRepository.save(file);
            return true;
        }else {
            logger.error("file move failed");
            return false;
        }

    }

    /**
     * Check whether the user has the authority to operate the note
     * @param noteID the note ID
     * @throws NoteNotExistException if the note does not exist
     * @throws ModifyNotAuthoredException if the user has no authority to modify the note
     * @return the note been authorized
     */
    public Note checkNoteAuthority(String noteID)
            throws NoteNotExistException, ModifyNotAuthoredException {
        Note note = noteRepository.findNoteByNoteID(noteID);
        if (note == null) {
            throw new NoteNotExistException("note not found");
        }
        checkNoteAuthority(note);
        return note;
    }
    public void checkNoteAuthority(Note note) {
        String notebookID = note.getNotebookID();
        if (!notebookService.checkAuthority(StpUtil.getLoginIdAsInt(), notebookID)) {
            throw new ModifyNotAuthoredException("user has no authority to modify this note");
        }
    }

    public Files checkFileAuthority(String fileID)
            throws FileNotExistException, ModifyNotAuthoredException {
        Files file = fileRepository.findFilesByFileID(fileID);
        if (file == null) {
            throw new FileNotExistException("file not found");
        }
        checkNoteAuthority(file.getNote());
        return file;
    }
}
