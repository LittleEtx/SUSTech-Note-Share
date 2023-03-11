package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    public List<Note> findNotesByNoteID(int noteID);
}
