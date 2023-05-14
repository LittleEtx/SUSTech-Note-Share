package com.example.SUSTechNote.app;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/group")
public class GroupApp {
    private final Logger logger = LoggerFactory.getLogger(UserDetailApp.class);
    private final GroupService groupService;

    public GroupApp(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/loadJoinedGroup")
    public ResponseEntity<?> loadJoinedGroup() {
//        int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        return ResponseEntity.ok(groupService.loadJoinedGroup(userID));
    }

    @GetMapping("/loadEnjoinedGroup")
    public ResponseEntity<?> loadEnjoinedGroup() {
//        int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        return ResponseEntity.ok(groupService.loadEnjoinedGroup(userID));
    }

    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@RequestBody JSONObject jsonObject) {
//        int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        String groupName = jsonObject.getString("groupName");
        String groupDescription = jsonObject.getString("groupDescription");
        String createTime = jsonObject.getString("createTime");
        String groupOwnerName = groupService.findUserNameByUserID(userID);
        try {
            groupService.createGroup(userID, groupName, groupDescription, createTime, groupOwnerName);
            return ResponseEntity.ok("Group created successfully!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Group creaated fail \n" + e.getMessage());
        }

    }

    @PostMapping("/joinGroup")
    public ResponseEntity<?> joinGroup(@RequestBody JSONObject jsonObject) {
//        int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        int groupID = jsonObject.getInteger("groupID");
        try {
            groupService.joinGroup(userID, groupID);
            return ResponseEntity.ok("Join group successfully!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Join group fail \n" + e.getMessage());
        }
    }

    @PostMapping("/updateGroup")
    public ResponseEntity<?> updateGroup(@RequestBody JSONObject jsonObject) {
//      int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        int groupID = jsonObject.getInteger("groupID");
        String groupName = jsonObject.getString("groupName");
        String groupDescription = jsonObject.getString("groupDescription");
        try {
            String result = groupService.updateGroup(userID, groupID, groupName, groupDescription);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Update group fail \n" + e.getMessage());
        }
    }

    @PostMapping("/quitGroup")
    public ResponseEntity<?> quitGroup(@RequestBody JSONObject jsonObject) {
//        int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        int groupID = jsonObject.getInteger("groupID");
        try {
            groupService.quitGroup(userID, groupID);
            return ResponseEntity.ok("Quit group successfully!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Quit group fail \n" + e.getMessage());
        }
    }

    @PostMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(@RequestBody JSONObject jsonObject) {
//        int userID = StpUtil.getLoginIdAsInt();
        int userID = 12112628;
        int groupID = jsonObject.getInteger("groupID");
        try {
            groupService.deleteGroup(userID, groupID);
            return ResponseEntity.ok("Delete group successfully!");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Delete group fail \n" + e.getMessage());
        }
    }

    @PostMapping("/groupInfo")
    public ResponseEntity<?> groupInfo(@RequestBody JSONObject jsonObject) {
        int groupID = jsonObject.getInteger("groupID");
        try {
            Group group = groupService.groupInfo(groupID);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Get group info fail \n" + e.getMessage());
        }
    }

    @PostMapping("/groupMembers")
    public ResponseEntity<?> groupMembers(@RequestBody JSONObject jsonObject) {
        int groupID = jsonObject.getInteger("groupID");
        try {
            return ResponseEntity.ok(groupService.groupMembers(groupID));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Get group members fail \n" + e.getMessage());
        }
    }

    @PostMapping("/groupNotebooksInfo")
    public ResponseEntity<?> groupNotebooksInfo(@RequestBody JSONObject jsonObject) {
        int groupID = jsonObject.getInteger("groupID");
        try {
            return ResponseEntity.ok(groupService.groupNotebookInfo(groupID));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body("Get group notebook info fail \n" + e.getMessage());
        }
    }


}
