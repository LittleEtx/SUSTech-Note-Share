package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
