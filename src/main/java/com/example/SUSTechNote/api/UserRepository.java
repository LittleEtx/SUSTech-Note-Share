package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findUsersByUserIDAndPassword(int userID, String password);
    public List<User> findUsersByUserID(int userID);

    public User findUserByUserID(Integer userID);
}
