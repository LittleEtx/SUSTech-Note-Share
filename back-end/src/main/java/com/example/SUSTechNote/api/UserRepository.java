package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUsersByUserIDAndPassword(int userID, String password);
    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    boolean existsByUserID(Integer userID);

    User findUserByUserID(int userID);

    User findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update users set avatar = ?1 where userid = ?2", nativeQuery = true)
    void updateAvatar(String avatar, int userID);

    @Query(value = "select max(userid) from users", nativeQuery = true)
    int getMaxID();
}
