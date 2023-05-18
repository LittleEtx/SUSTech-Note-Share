package com.example.SUSTechNote.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    /**
     * 上传文件
     * @param noteID 文件所属的笔记ID
     * @param fileName 文件名
     * @param file 文件
     * @return 文件ID
     */
    String uploadFile(String noteID, String fileName, MultipartFile file) throws IOException;

    boolean updateFile(MultipartFile file, String fileID);

    /**
     * 删除文件
     * @param fileID 文件ID
     * @return 是否成功
     * @throws com.example.SUSTechNote.exception.FileNotExistException 文件不存在
     */
    boolean deleteFile(String fileID);

    void renameFile(String fileID, String newName);

    boolean moveFile(String fileID, String noteID);

}
