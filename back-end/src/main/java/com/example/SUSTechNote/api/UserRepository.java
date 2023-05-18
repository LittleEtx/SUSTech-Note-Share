package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUsersByUserIDAndPassword(int userID, String password);
    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    boolean existsByUserID(Integer userID);

    User findUserByUserID(int userID);

    User findUserByEmail(String email);

    @Query(value = "SELECT userid,user_name,email,avatar,description,gender,birth FROM users WHERE user_name LIKE ?1 OR email LIKE ?1 or userID LIKE ?1 ORDER BY user_name DESC", nativeQuery = true)
    Page<Map<String,Object>> searchUsersWithLimit(String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update users set avatar = ?1 where userid = ?2", nativeQuery = true)
    void updateAvatar(String avatar, int userID);

    @Query(value = "select max(userid) from users", nativeQuery = true)
    int getMaxID();
}
