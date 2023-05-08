package com.example.SUSTechNote.api;
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
    @Query(value = "update notebooks set status = ?1 where notebookid = ?2", nativeQuery = true)
    void changeStatusByNotebookID(Integer status, String notebookID);

    @Query(value = "select count(*) from notebooks where authorid = ?1", nativeQuery = true)
    int findNotebookCountByAuthorID(int userID);

    @Query(value = "select * from notebooks where authorid = ?1", nativeQuery = true)
    List<Notebook> findNotebookByAuthorID(int userID);

    @Query(value = "select cover from notebooks where notebookid = ?1", nativeQuery = true)
    String findCoverByNotebookID(String notebookID);

    @Modifying
    @Transactional
    @Query(value = "update notebooks set cover = ?1 where notebookid = ?2", nativeQuery = true)
    void updateCover(String Cover, String notebookID);

}
