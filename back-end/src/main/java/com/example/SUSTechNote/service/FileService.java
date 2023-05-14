package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

@Service
public interface FileService {
    String uploadFile(String noteID, String fileName,MultipartFile file) throws IOException;
    void addFile(String fileID, String fileName, String fileUrl, String noteID);

    void deleteFile(String noteID,String fileName);

    void renameFile(String noteID,String fileName,String newName);

}
