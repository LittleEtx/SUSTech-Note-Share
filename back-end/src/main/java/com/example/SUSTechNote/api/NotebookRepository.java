package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Group;
import com.example.SUSTechNote.entity.Notebook;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotebookRepository extends JpaRepository<Notebook, Integer> {
    List<Notebook> findNotebooksByNotebookID(String notebookID);

    @Modifying
    @Transactional
    @Query(value = "delete from notebooks where notebookid = ?1", nativeQuery = true)
    void deleteNotebook(String notebookID);

    @Query(value = "select count(*) from notebooks where authorid = ?1", nativeQuery = true)
    int findNotebookCountByAuthorID(int userID);

    @Modifying
    @Transactional
    @Query(value = "update notebooks set notebook_name = ?2, tag = ?3, description = ?4 where notebookid = ?1", nativeQuery = true)
    void updateNotebook(String notebookID, String notebookName, String tag, String description);

    @Query(value = "select * from notebooks " +
            "where authorid = ?1 " +
            "and remove_time is null", nativeQuery = true)
    List<Notebook> findNotebookByAuthorID(int userID);

    @Query(value = "select cover from notebooks where notebookid = ?1", nativeQuery = true)
    String findCoverByNotebookID(String notebookID);

    @Query(value = "select * from notebooks " +
            "where authorid = ?1 " +
            "and is_public = 1 " +
            "and remove_time IS NULL", nativeQuery = true)
    List<Notebook> findPublicNotebooksByAuthorID(int userID);

    @Modifying
    @Transactional
    @Query(value = "update notebooks set cover = ?1 where notebookid = ?2", nativeQuery = true)
    void updateCover(String Cover, String notebookID);

    @Modifying
    @Transactional
    @Query(value = "update notebooks set directory = ?3 where directory = ?2 and authorid = ?1", nativeQuery = true)
    void renameDir(int userID, String oldName, String newName);

    @Query(value = "select authorid from notebooks where notebookid = ?1", nativeQuery = true)
    Integer findAuthorIDByNotebookID(String notebookID);

    @Query(value = "select * from notebooks where notebookid = ?1", nativeQuery = true)
    Notebook findNotebookByNotebookID(String notebookID);

    @Query(value = "select notebookid from notebooks where authorid = ?1", nativeQuery = true)
    List<String> findNotebookIDByAuthorID(int userID);

    @Query(value = "select * from notebooks " +
            "where notebookid in " +
            "(select notebookid from notebook_share_user where userid = ?1) " +
            "and remove_time is null", nativeQuery = true)
    List<Notebook> findSharedNotebooksByUserID(int userID);

    @Query(value = "select * from my_groups " +
            "where groupid in " +
            "(select groupid from notebook_share_group where notebookid = ?1) ", nativeQuery = true)
    List<Group> getSharedGroups(String notebookID);
}
