package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

@Service
public interface FileService {
    /**
     * 上传文件
     * @param noteID 文件所属的笔记ID
     * @param fileName 文件名
     * @param file 文件
     * @param originID 若替换原先文件，需提供原先文件的ID
     * @return 文件ID
     */
    String uploadFile(String noteID, String fileName, MultipartFile file, String originID) throws IOException;

    void deleteFile(String noteID,String fileName);

    void renameFile(String noteID,String fileName,String newName);

}
