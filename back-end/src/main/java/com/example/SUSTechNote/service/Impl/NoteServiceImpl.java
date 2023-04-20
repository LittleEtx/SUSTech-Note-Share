package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.NoteRepository;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);
    private final NoteRepository NoteRepository;
    public NoteServiceImpl(NoteRepository NoteRepository) {
        this.NoteRepository = NoteRepository;
    }

    @Override
    public void addNote(String noteID, int userID, String noteBookID, String realPath, String title, int isPublic){
        try {
            Note note = new Note();
            note.setNoteID(noteID);
            note.setAuthorID(userID);
            note.setNotebookID(noteBookID);
            note.setSavingPath(realPath);
            note.setNoteName(title);
            note.setIsPublic(isPublic);
            note.setStatus(0);
            NoteRepository.save(note);
        } catch (Exception e) {
            logger.error("addNote error: " + e.getMessage());
        }
    }

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
    }

    @Override
    public int checkNote(String NoteID){
        List<Note> Notes = NoteRepository.findNotesByNoteID(NoteID);
        if (Notes.size() == 1) {
            return 1;
        } else if (Notes.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    }

    @Override
    public int deleteNote(String NoteID){
        if (checkNote(NoteID) == 1){
            NoteRepository.deleteNotesByNoteID(NoteID);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Note> findAllNote(){
        return NoteRepository.findAll();
    }

    @Override
    public int findNotesCountByNotebookID(String notebookID) {
        return NoteRepository.findNotesByNotebookID(notebookID);
    }
}
