package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<Files, Integer> {
    @Query(value = "select * from files where note_id in (select noteid from notes where notes.notebookid = ?1)", nativeQuery = true)
    List<Files> findFilesByNotebook(String notebookID);

    Files findFilesByNoteAndFileName(Note note, String fileName);

    List<Files> findFilesByNote(Note note);

    List<Files> findFilesByNote_NoteID(String noteID);
    Files findFilesByFileID(String fileID);
}
