package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {
}
