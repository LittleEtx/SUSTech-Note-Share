package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Note;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {

    public int addNote(String noteID,Integer isPublic );

    public int updateNote(Note note);

    public int checkNote(String noteID);

    public int deleteNote(String noteID);

    public List<Note> findAllNote();
}
