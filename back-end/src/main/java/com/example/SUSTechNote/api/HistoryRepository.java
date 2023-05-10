package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {

}
