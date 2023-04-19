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
    public List<User> findUsersByUserIDAndPassword(int userID, String password);
    public List<User> findUsersByUserID(int userID);
    public User findUserByUserID(int userID);
    public User findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update users set avatar = ?1 where userid = ?2", nativeQuery = true)
    public void updateAvatar(String avatar, int userID);



}
