package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface NoteService {
    Map<Note, List<Files>> findNotesUnderNotebook(String notebookID);

    void addNote(String noteID, int userID, String noteBookID, String realPath, String title, int isPublic);

    int updateNote(Note note);

    int checkNote(String noteID);

    String deleteNote(String noteID);

    List<Note> findAllNote();

    int findNotesCountByNotebookID(String notebookID);

    String getNoteNameByNoteID(String noteID);

    List<String> findNoteIDsByNotebookID(String noteBookID);
}
