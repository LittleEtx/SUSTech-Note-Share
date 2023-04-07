package com.example.SUSTechNote.api;
import com.example.SUSTechNote.entity.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookRepository extends JpaRepository<Notebook, Integer> {
    public List<Notebook> findNotebooksByNotebookID(String notebookID);
    public void deleteNotebooksByNotebookID(String notebookID);

    public Notebook findNotebookByNotebookID(String notebookID);
}
