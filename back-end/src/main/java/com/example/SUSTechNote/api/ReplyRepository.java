package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Reply;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    Reply findReplyByReplyID(String ReplyID);
    void deleteReplyByReplyID(String ReplyID);

    @Modifying
    @Transactional
    @Query(value = "delete from reply where comment_id = ?1 and replyid = ?2", nativeQuery = true)
    void deleteReply(String commentID, String reply);

    @Query(value = "select user_id from reply where comment_id = ?1 and replyid = ?2", nativeQuery = true)
    int getReplyUserID(String commentID, String reply);

    @Query(value = "select authorid from notebooks where notebookid = (select notebook_id from comment where commentid = ?1)", nativeQuery = true)
    int getNotebookUserID(String commentID);

    @Query(value = "select replyid from reply where comment_id = ?1", nativeQuery = true)
    List<String> getReplyIDsByCommentID(String commentID);

    @Modifying
    @Transactional
    @Query(value = "insert into reply(replyid, to_user_name, reply_content, reply_time, comment_id, user_id) values(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
    void createReply(String newReplyID, String toUserName, String content, LocalDateTime replyTime, String commentID, int userID);
}
