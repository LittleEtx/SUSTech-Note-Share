package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.FileRepository;
import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.FileService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileRepository fileRepository;

    private final StaticPathHelper staticPathHelper;
    private final AuthorityService authorityService;

    public FileServiceImpl(FileRepository fileRepository, StaticPathHelper staticPathHelper,
                           AuthorityService authorityService) {
        this.fileRepository = fileRepository;
        this.staticPathHelper = staticPathHelper;
        this.authorityService = authorityService;
    }

    // 需加入同步锁防止同时上传多个文件时出现文件ID重复的情况
    @Override
    public synchronized String uploadFile(String noteID, String fileName,
                                          MultipartFile file) throws IOException {
        Note note = authorityService.checkNoteAuthority(noteID);
//        var noteID = note.getNoteID();
        var files = fileRepository.findFilesByNotebook(noteID);
        int lastFileNum = files.stream()
                .map(f -> Integer.parseInt(f.getFileID().substring(f.getFileID().lastIndexOf("_") + 1)))
                .max(Integer::compareTo).orElse(0);
        // 新建文件，生成新的文件ID, 注意实际保存到文件夹的时候应该保留文件后缀名，但是存入数据库时应该去掉
        String fileType = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String fileID = noteID + "_" + (lastFileNum + 1) + fileType;

        // 若笔记的文件夹不存在，则创建
        File noteFile = new File(staticPathHelper.getStaticPath(), note.getSavingPath());
        if (!noteFile.exists()) {
            logger.warn("note dir not exist, create new one: " + noteFile.getAbsolutePath());
            if (!noteFile.mkdirs()) {
                logger.error("create note file failed: " + noteFile.getAbsolutePath());
                throw new IOException("create note file failed");
            }
        }
        String relativePath = note.getSavingPath() + "/" + fileID;
        File targetFile = new File(noteFile, fileID);
        file.transferTo(targetFile); // this method will automatically delete file if already exist
        String fileUrl = "/api/static" + relativePath;

        fileID = fileID.substring(0, fileID.lastIndexOf(".")); // 去掉文件后缀名
        fileRepository.save(new Files(fileID, fileName, fileUrl,
                relativePath, file.getContentType(), note));
        logger.debug("add file " + fileName + " to "
                + targetFile.getAbsolutePath() + " successfully, id=" + fileID);
        return fileID;
    }

    @Override
    public boolean updateFile(MultipartFile file, String fileID) {
        var fileInfo = authorityService.checkFileAuthority(fileID);
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
    public boolean deleteFile(String fileID) {
        Files file = authorityService.checkFileAuthority(fileID);
        File filePath = new File(staticPathHelper.getStaticPath(), file.getSavingPath());
        if (!filePath.exists() || !filePath.isFile()) {
            logger.error("file not found, clean record in database");
            fileRepository.delete(file);
            return true;
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
    public void renameFile(String fileID, String newName) {
        Files file = authorityService.checkFileAuthority(fileID);
        file.setFileName(newName);
        fileRepository.save(file);
    }


    // 需加入同步锁防止同时移动多个文件时出现文件ID重复的情况
    @Override
    public synchronized boolean moveFile(String fileID, String noteID) {
        Files file = authorityService.checkFileAuthority(fileID);
        Note note = authorityService.checkNoteAuthority(noteID);
        if (!Objects.equals(file.getNote().getNotebookID(), note.getNotebookID())) {
            logger.error("file and note not in the same notebook");
            return false;
        }
        var relativeSavingPath = note.getSavingPath() + "/" + fileID;
        if (relativeSavingPath.equals(file.getSavingPath())) {
            logger.trace("move file to itself");
            return true;
        }

        //判断目标位置是否已存在同名文件
        File originFile = new File(staticPathHelper.getStaticPath(), file.getSavingPath());
        File destinationFile = new File(staticPathHelper.getStaticPath(), relativeSavingPath);
        if (destinationFile.exists()) {
            logger.error("file existed in target note");
            return false;
        }
        System.out.println();
        logger.debug("rename file " + originFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
        if (originFile.renameTo(destinationFile)) {
            //更新数据库
            file.setFileUrl("/api/static" + relativeSavingPath);
            file.setNote(note);
            file.setSavingPath(relativeSavingPath);
            fileRepository.save(file);
            return true;
        } else {
            logger.error("file move failed");
            return false;
        }

    }
}
