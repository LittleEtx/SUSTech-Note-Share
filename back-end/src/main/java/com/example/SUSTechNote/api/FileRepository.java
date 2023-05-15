package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<Files, Integer> {
    @Query(value = "select count(*) from files where note_id = ?1", nativeQuery = true)
    int findFileCountByNote(String noteID);

    Files findFilesByNoteAndFileName(Note note, String fileName);

    List<Files> findFilesByNote(Note note);
}
