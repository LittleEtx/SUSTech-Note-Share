package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.FileRepository;

import com.example.SUSTechNote.api.NoteRepository;
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

@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileRepository fileRepository;

    private final StaticPathHelper staticPathHelper;
    private final NoteRepository noteRepository;

    public FileServiceImpl(FileRepository fileRepository, StaticPathHelper staticPathHelper,
                           NoteRepository noteRepository) {
        this.fileRepository = fileRepository;
        this.staticPathHelper = staticPathHelper;
        this.noteRepository = noteRepository;
    }
    @Override
    public String uploadFile(String noteID,String fileName, MultipartFile file)throws IOException{
        String basePath = staticPathHelper.getStaticPath();
        String userID = StpUtil.getLoginIdAsString();
        int index = noteID.indexOf("_", noteID.indexOf("_")+1);//找到第二个“_”的位置
        String savePath = basePath + "/notebooks/" + userID + "/" + noteID.substring(0,index) + "/" + noteID;
        String fileUrl = savePath + "/" +fileName;
        File existingFile = new File(fileUrl);
        try {
            if (existingFile.exists()) {
                existingFile.delete();
            }
            file.transferTo(new File(fileUrl));
            Note note = noteRepository.findNoteByNoteID(noteID);
            System.out.println(note);
            int count = fileRepository.findFileCountByNote(note.getNoteID());
            System.out.println(count);
            String fileID = noteID + "_" + count;
            addFile(fileID,fileName,fileUrl,noteID);
            return fileUrl;
        } catch (Exception e) {
            logger.error("uploadFile error: " + e.getMessage());
        }
        return null;
    }
    @Override
    public void addFile(String fileID, String fileName, String fileUrl,String noteID){
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
            if (checkFileExistbyNoteandFileinDatabase(note,fileDatabase)){
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


    public boolean checkFileExistbyNoteandFileinDatabase(Note note, Files fileDatabase) {
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
    }
