package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Note;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {

    void addNote(String noteID, int userID, String noteBookID, String realPath, String title, int isPublic);

    int updateNote(Note note);

    int checkNote(String noteID);

    void deleteNote(String noteID);

    List<Note> findAllNote();

    int findNotesCountByNotebookID(String notebookID);
}
