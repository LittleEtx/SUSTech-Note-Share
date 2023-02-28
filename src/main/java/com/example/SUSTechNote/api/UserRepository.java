package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findUsersByUserIDAndPassword(int userId, String password);
}
