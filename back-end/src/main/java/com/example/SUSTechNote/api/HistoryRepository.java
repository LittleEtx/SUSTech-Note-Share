package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.History;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Modifying
    @Transactional
    @Query(value = "insert into history (userID, notebookID, visit_time) values (?1, ?2, ?3)", nativeQuery = true)
    void createHistory(int userID, String notebookID, LocalDateTime visitTime);

    @Query(value = "select * from history where userID = ?1", nativeQuery = true)
    List<History> getHistory(int userID);

    @Modifying
    @Transactional
    @Query(value = "delete from history where userID = ?1 and historyID = ?2", nativeQuery = true)
    void deleteHistory(int userID, int historyID);

    @Modifying
    @Transactional
    @Query(value = "delete from history where userID = ?1", nativeQuery = true)
    void deleteAllHistory(int userID);
}
