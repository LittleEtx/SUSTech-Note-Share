package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Note;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Modifying
    @Transactional
    @Query(value = "delete from notes where noteid = ?1", nativeQuery = true)
    void deleteNotesByNoteID(String noteID);

    @Query(value = "select count(*) from notes where notebookid = ?1", nativeQuery = true)
    int findNoteCountByNotebookID(String notebookID);

    List<Note> findNotesByNotebookID(String notebookID);

    Note findNoteByNoteID(String noteID);


    @Query(value = "select noteid from notes where notebookid = ?1", nativeQuery = true)
    List<String> findNoteIDsByNotebookID(String noteBookID);
}
