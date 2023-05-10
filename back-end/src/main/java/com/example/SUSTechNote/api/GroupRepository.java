package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Group;
import jakarta.transaction.Transactional;
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
}
