package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Note;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {

    public int addNote(Integer noteID,String content,Integer isPublic );

    public int updateNote(Note note);

    public int checkNote(Integer noteID);

    public int deleteNote(Integer noteID);

    public List<Note> findAllNote();
}
