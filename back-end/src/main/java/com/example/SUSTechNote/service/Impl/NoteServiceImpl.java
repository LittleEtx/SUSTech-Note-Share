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
    private final NoteRepository noteRepository;
    public NoteServiceImpl(NoteRepository NoteRepository) {
        this.noteRepository = NoteRepository;
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
            noteRepository.save(note);
        } catch (Exception e) {
            logger.error("addNote error: " + e.getMessage());
        }
    }

    @Override
    public int updateNote(Note Note){
        if (checkNote(Note.getNoteID()) == 1 ){
            noteRepository.save(Note);
            return 1;
        }
        if (checkNote(Note.getNoteID()) == 400){
            return 400;
        }
        return 0;
    }

    @Override
    public int checkNote(String NoteID){
        List<Note> Notes =noteRepository.findNotesByNoteID(NoteID);
        if (Notes.size() == 1) {
            return 1;
        } else if (Notes.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    }

    @Override
    public String deleteNote(String NoteID){
        try {
            noteRepository.deleteNotesByNoteID(NoteID);
            return "Note deleted successfully";
        } catch (Exception e) {
            logger.error("deleteNote error: " + e.getMessage());
            return "Note deleted failed\n" + e.getMessage() + "\n";
        }
    }

    @Override
    public List<Note> findAllNote(){
        return noteRepository.findAll();
    }

    @Override
    public int findNotesCountByNotebookID(String notebookID) {
        return noteRepository.findNotesByNotebookID(notebookID);
    }

    @Override
    public String getNoteNameByNoteID(String noteID) {
        return noteRepository.findNoteNameByNoteID(noteID);
    }

    @Override
    public List<String> findNoteIDsByNotebookID(String noteBookID) {
        return noteRepository.findNoteIDsByNotebookID(noteBookID);
    }
}
