package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findNotesByNoteID(String noteID);
    void deleteNotesByNoteID(String noteID);

    @Query(value = "select count(*) from notes where notebookid = ?1", nativeQuery = true)
    int findNotesByNotebookID(String notebookID);
}
