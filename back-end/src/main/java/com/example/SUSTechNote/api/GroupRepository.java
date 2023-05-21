package com.example.SUSTechNote.api;

import com.alibaba.fastjson.JSONObject;
import com.example.SUSTechNote.entity.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(value = "select group_id from user_group where user_id = ?1", nativeQuery = true)
    List<Integer> findGroupIDByUserID(int userID);

    @Query(value = "select user_name from users where userid = ?1", nativeQuery = true)
    String findUserNameByUserID(int userID);

    @Modifying
    @Transactional
    @Query(value = "insert into user_group values (?1, ?2);", nativeQuery = true)
    void addUserToGroup(int userID, int groupID);

    @Modifying
    @Transactional
    @Query(value = "delete from user_group where user_id = ?1 and group_id = ?2", nativeQuery = true)
    void deleteUserFromGroup(int userID, int groupID);

    @Modifying
    @Transactional
    @Query(value = "delete from user_group where group_id = ?1", nativeQuery = true)
    void deleteUsersInGroup(int groupID);

    @Modifying
    @Transactional
    @Query(value = "delete from my_groups where group_ownerid = ?1 and groupid = ?2", nativeQuery = true)
    void deleteGroup(int userID, int groupID);

    @Query(value = "select user_id from user_group where group_id = ?1", nativeQuery = true)
    List<Integer> findGroupMembers(int groupID);

    @Query(value = "select notebookid from notebook_share_group where groupid = ?1", nativeQuery = true)
    List<String> findGroupNotebooksByGroupID(int groupID);

    @Query(value = "select * from my_groups where groupid = ?1", nativeQuery = true)
    Group findGroupByGroupID(int i);

    @Query(value = "SELECT groupid,group_description,group_name,group_ownerid,group_owner_name,create_time " +
            "FROM (select * from user_group left join my_groups  on user_group.group_id = my_groups.groupid) as gp " +
            "WHERE (groupid LIKE ?1 OR group_name LIKE ?1) AND user_id = ?2", nativeQuery = true)
//    select * from my_groups where groupid in
//    (select groupid from user_group where user_id = ?1) and
//    (groupid like ?2 or my_groups.group_name like ?2);
    Page<JSONObject> searchGroupsWithLimit(String keyword, int userID, Pageable pageable);

}
