package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.GroupRepository;
import com.example.SUSTechNote.api.NotebookRepository;
import com.example.SUSTechNote.api.UserRepository;
import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.entity.User;
import com.example.SUSTechNote.exception.AccountNotExistException;
import com.example.SUSTechNote.exception.GroupNotExistException;
import com.example.SUSTechNote.service.NotebookService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class NotebookServiceImpl implements NotebookService {

    private final Logger logger = LoggerFactory.getLogger(NotebookServiceImpl.class);
    private final NotebookRepository notebookRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final StaticPathHelper staticPathHelper;
    private final AuthorityService authorityService;

    public NotebookServiceImpl(NotebookRepository notebookRepository, UserRepository userRepository, GroupRepository groupRepository, StaticPathHelper staticPathHelper, AuthorityService authorityService) {
        this.notebookRepository = notebookRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.staticPathHelper = staticPathHelper;
        this.authorityService = authorityService;
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
    public List<Notebook> findNotebooks(int userID) {
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
    public List<String> findNotebookIDByUserID(int userID) {
        return notebookRepository.findNotebookIDByAuthorID(userID);
    }

    @Override
    public List<Notebook> findSharedNotebooks(int userID) {
        try {
            return notebookRepository.findSharedNotebooksByUserID(userID);
        } catch (Exception e) {
            logger.error("findSharedNotebooks error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean ifLike(String notebookID) {
        int userID = StpUtil.getLoginIdAsInt();
        List<String> notebookIDs = notebookRepository.findLikeNotebookIDByUserID(userID, notebookID);
        return notebookIDs.size() == 1;
    }

    @Override
    public boolean likeNotebook(String notebookID) {
        int userID = StpUtil.getLoginIdAsInt();
        try {
            notebookRepository.likeNotebook(notebookID, userID);
            notebookRepository.addLikeCount(notebookID);
            return true;
        } catch (Exception e) {
            logger.error("likeNotebook error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String setNotebookPrivate(String notebookID) {
        int userID = StpUtil.getLoginIdAsInt();
        int ownerID = notebookRepository.findAuthorIDByNotebookID(notebookID);
        if(userID != ownerID) {
            return "You are not the owner of this notebook.";
        } else {
            try {
                notebookRepository.clearLikeAndStarAndSetPrivate(notebookID);
                notebookRepository.removeLikeUser(notebookID);
                notebookRepository.removeStarUser(notebookID);
                notebookRepository.removeReply(notebookID);
                notebookRepository.removeComment(notebookID);
                return "Set success!";
            } catch (Exception e) {
                logger.error("setNotebookPrivate error: " + e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public String setNotebookPublic(String notebookID) {
        int userID = StpUtil.getLoginIdAsInt();
        int ownerID = notebookRepository.findAuthorIDByNotebookID(notebookID);
        if (userID != ownerID) {
            return "You are not the owner of this notebook.";
        } else {
            try {
                notebookRepository.setPublic(notebookID);
                return "Set success!";
            } catch (Exception e) {
                logger.error("setNotebookPublic error: " + e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public boolean shareToUser(String notebookID, int userID) {
        Notebook notebook = authorityService.checkNotebookAuthority(notebookID);
        User newUser = userRepository.findUserByUserID(userID);
        if (newUser == null) {
            throw new AccountNotExistException("User not found.");
        }
        if (notebook.getUsers().contains(newUser)) {
            logger.info("notebook " + notebookID + " already shared to user " + userID);
            return false;
        }
        try {
            notebookRepository.shareToUser(notebookID, userID);
            return true;
        } catch (Exception e) {
            logger.error("shareToUser error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String shareToGroup(String notebookID, int groupID) {
        Notebook notebook = authorityService.checkNotebookAuthority(notebookID);
        Group newGroup = groupRepository.findGroupByGroupID(groupID);
        if (newGroup == null) {
            throw new GroupNotExistException("Group not found.");
        }
        if (notebook.getGroups().contains(newGroup)) {
            logger.info("notebook " + notebookID + " already shared to group " + groupID);
            return "Notebook already shared to this group.";
        }
        try {
            notebookRepository.shareToGroup(notebookID, groupID);
            return "Share to group successfully.";
        } catch (Exception e) {
            logger.error("shareToGroup error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String cancelUserShare(String notebookID, int userID) {
        Notebook notebook = authorityService.checkNotebookAuthority(notebookID);
        User userToRemove = userRepository.findUserByUserID(userID);
        if (userToRemove == null) {
            throw new AccountNotExistException("User not found.");
        }
        if (!notebook.getUsers().contains(userToRemove)) {
            logger.info("notebook " + notebookID + " not shared to user " + userID);
            return "Notebook not shared to this user.";
        }
        try {
            notebookRepository.cancelUserShare(notebookID, userID);
            return "Cancel successfully.";
        } catch (Exception e) {
            logger.error("cancelUserShare error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String cancelGroupShare(String notebookID, int groupID) {
        Notebook notebook = authorityService.checkNotebookAuthority(notebookID);
        Group groupToRemove = groupRepository.findGroupByGroupID(groupID);
        if (groupToRemove == null) {
            throw new GroupNotExistException("Group not found.");
        }
        if (!notebook.getGroups().contains(groupToRemove)) {
            logger.info("notebook " + notebookID + " not shared to group " + groupID);
            return "Notebook not shared to this group.";
        }
        try {
            notebookRepository.cancelGroupShare(notebookID, groupID);
            return "Cancel successfully.";
        } catch (Exception e) {
            logger.error("cancelGroupShare error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public  List<Map<String,Object>> searchPublicNotebookWithLimit(String key, int limit){
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "notebook_name"));
        key = "%" + key + "%";
        Page<Map<String,Object>> notebooks = notebookRepository.searchPublicNotebookWithLimit(key,pageRequest);
        return notebooks.getContent();
    }
}
