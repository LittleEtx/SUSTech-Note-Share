package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Note;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findNotesByNoteID(String noteID);

    @Modifying
    @Transactional
    @Query(value = "delete from notes where noteid = ?1;", nativeQuery = true)
    void deleteNotesByNoteID(String noteID);

    @Query(value = "select count(*) from notes where notebookid = ?1", nativeQuery = true)
    int findNotesByNotebookID(String notebookID);

    Note findNoteByNoteID(String noteID);

    @Query(value = "select note_name from notes where noteid = ?1", nativeQuery = true)
    String findNoteNameByNoteID(String noteID);

    @Query(value = "select noteid from notes where notebookid = ?1", nativeQuery = true)
    List<String> findNoteIDsByNotebookID(String noteBookID);
}
