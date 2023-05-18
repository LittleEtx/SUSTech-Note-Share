package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Comment;
import com.example.SUSTechNote.entity.Reply;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "select commentid from comment where notebook_id = ?1", nativeQuery = true)
    List<String> getCommentIDsByNotebookID(String notebookID);

    @Modifying
    @Transactional
    @Query(value = "insert into comment(commentid, comment_content, comment_time, notebook_id, user_id) values(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
    void createComment(String newCommentID, String comment, LocalDateTime commentTime, String notebookID, int userID);

    @Query(value = "select user_id from comment where commentid = ?1", nativeQuery = true)
    int getCommentUserID(String commentID);

    @Query(value = "select authorid from notebooks where notebookid = (select notebook_id from comment where commentid = ?1)", nativeQuery = true)
    int getNotebookUserID(String commentID);

    @Modifying
    @Transactional
    @Query(value = "delete from reply where comment_id = ?1", nativeQuery = true)
    void deleteRepliesUnderComment(String commentID);

    @Modifying
    @Transactional
    @Query(value = "delete from comment where commentid = ?1", nativeQuery = true)
    void deleteComment(String commentID);

    @Query(value = "select * from comment where notebook_id = ?1", nativeQuery = true)
    List<Comment> getCommentsByNotebookID(String notebookID);

    @Query(value = "select * from reply where comment_id = ?1", nativeQuery = true)
    List<Reply> getRepliesByCommentID(String commentID);
}
