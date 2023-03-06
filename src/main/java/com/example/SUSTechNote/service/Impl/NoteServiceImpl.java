package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.NoteRepository;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteRepository NoteRepository;

    @Override
    public int addNote(Integer noteID,String content,Integer isPublic){
        if (checkNote(noteID) == 0 ){
            Note note = new Note();
            note.setNoteID(noteID);
            note.setContent(content);
            note.setIsPublic(isPublic);
            NoteRepository.save(note);
            return 1;
        }
        if (checkNote(noteID) == 400){
            return 400;
        }
        return 0;
    };

    @Override
    public int updateNote(Note Note){
        if (checkNote(Note.getNoteID()) == 1 ){
            NoteRepository.save(Note);
            return 1;
        }
        if (checkNote(Note.getNoteID()) == 400){
            return 400;
        }
        return 0;
    };

    @Override
    public int checkNote(Integer NoteID){
        List<Note> Notes = NoteRepository.findNotesByNoteID(NoteID);
        if (Notes.size() == 1) {
            return 1;
        } else if (Notes.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    };

    @Override
    public int deleteNote(Integer NoteID){
        if (checkNote(NoteID) == 1){
            NoteRepository.deleteById(NoteID);
            return 1;
        }
        return 0;
    };

    @Override
    public List<Note> findAllNote(){
        return NoteRepository.findAll();
    };
}
