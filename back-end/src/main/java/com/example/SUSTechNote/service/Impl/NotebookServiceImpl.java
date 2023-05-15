package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.NotebookRepository;

import com.example.SUSTechNote.entity.Notebook;

import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class NotebookServiceImpl implements NotebookService {

    private final Logger logger = LoggerFactory.getLogger(NotebookServiceImpl.class);
    private final NotebookRepository notebookRepository;

    private final StaticPathHelper staticPathHelper;

    public NotebookServiceImpl(NotebookRepository notebookRepository, StaticPathHelper staticPathHelper) {
        this.notebookRepository = notebookRepository;
        this.staticPathHelper = staticPathHelper;
    }
    
    @Override
    public void addNotebook(String noteBookID, int userID, String directory, String savingPath, String title, String tag, String description, int isPublic){
        try {
            LocalDateTime now = LocalDateTime.now();
            Notebook notebook = new Notebook();
            notebook.setNotebookID(noteBookID);
            notebook.setAuthorID(userID);
            notebook.setDirectory(directory);
            notebook.setSavePath(savingPath);
            notebook.setNotebookName(title);
            notebook.setTag(tag);
            notebook.setDescription(description);
            notebook.setIsPublic(isPublic);
            notebook.setUpdateTime(now);
            notebook.setLikeNum(0);
            notebook.setStar(0);
            notebookRepository.save(notebook);
        } catch (Exception e) {
            logger.error("addNotebook error: " + e.getMessage());
        }
    }

    @Override
    public boolean updateNotebook(String notebookID, String notebookName, String tag, String description){
        var notebook = getNotebookBasic(notebookID);
        try {
            notebookRepository.updateNotebook(
                    notebookID,
                    Objects.requireNonNullElse(notebookName, notebook.getNotebookName()),
                    Objects.requireNonNullElse(tag, notebook.getTag()),
                    Objects.requireNonNullElse(description, notebook.getDescription())
            );
        } catch (Exception e) {
            logger.error("updateNotebook error: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String deleteNotebook(String notebookID){
        try{
            notebookRepository.deleteNotebook(notebookID);
            return "Notebook deleted successfully.";
        } catch (Exception e) {
            logger.error("deleteNotebook error: " + e.getMessage());
            return "deleteNotebook error: " + e.getMessage();
        }
    }

    @Override
    public Notebook getNotebookBasic(String notebookID) {
        var notebooks = notebookRepository.findNotebooksByNotebookID(notebookID);
        return notebooks.size() == 1 ? notebooks.get(0) : null;
    }


    @Override
    public List<Notebook> findNotebooks() {
        int userID = StpUtil.getLoginIdAsInt();
        return notebookRepository.findNotebookByAuthorID(userID);
    }

    @Override
    public Notebook findNotebookByID(String notebookID){
        return notebookRepository.findNotebooksByNotebookID(notebookID).get(0);
    }

    @Override
    public int findNotebookCountByUserID(int userID) {
        return notebookRepository.findNotebookCountByAuthorID(userID);
    }

    @Override
    public List<Notebook> findPublicNotebooks(int userID) {
        return notebookRepository.findPublicNotebooksByAuthorID(userID);
    }

    @Override
    public String uploadCover(String notebookID, MultipartFile cover) throws IOException {
        //使用随机UUID作为文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") +
                ("image/jpeg".equals(cover.getContentType()) ? ".jpg" : ".png");
        String basePath = staticPathHelper.getStaticPath();
        int id = StpUtil.getLoginIdAsInt(); //获取用户ID
        String userID = String.valueOf(id);
        String savingPath = "/notebooks/"+userID+"/"+notebookID;
        File folder = new File(basePath, savingPath);
        if (!folder.exists()) {
            logger.debug("create dir for saving avatars at " + folder.getAbsolutePath());
            folder.mkdirs();
        }
        //add new cover to file
        cover.transferTo(new File(folder, fileName));
        //delete original cover
        String originalCover = notebookRepository.findCoverByNotebookID(notebookID);
        if (originalCover != null && !originalCover.equals("")) {
            String[] split = originalCover.split("/");
            String originalFileName = split[split.length - 1];
            File originalFile = new File(basePath, savingPath + "/" +  originalFileName);
            System.out.println(originalFile.getName());
            if (originalFile.exists()) {
                logger.debug("delete original cover at " + originalFile.getAbsolutePath());
                originalFile.delete();
            }
        }
        String url = "/api/static" + savingPath + "/" + fileName;
        logger.info("user " + userID + " update notebook cover " + notebookID + " url to: " + url);
        notebookRepository.updateCover(url, notebookID);
        return url;
    }

    @Override
    public void renameDir(int userID, String oldName, String newName) {
        notebookRepository.renameDir(userID, oldName, newName);
    }

    @Override
    public boolean checkAuthority(int userID, String notebookID) {
        int authorID = notebookRepository.findAuthorIDByNotebookID(notebookID);
        return authorID == userID;
    }

    @Override
    public List<String> findNotebookIDByUserID(int userID) {
        return notebookRepository.findNotebookIDByAuthorID(userID);
    }
}
