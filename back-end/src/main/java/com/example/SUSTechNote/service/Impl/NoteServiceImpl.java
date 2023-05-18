package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.FileRepository;
import com.example.SUSTechNote.api.NoteRepository;
import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.FileService;
import com.example.SUSTechNote.service.NoteService;
import com.example.SUSTechNote.util.StaticPathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteServiceImpl implements NoteService {

    private final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);
    private final NoteRepository noteRepository;
    private final FileRepository fileRepository;
    private final AuthorityService authorityService;
    private final FileService fileService;
    private final StaticPathHelper staticPathHelper;
    public NoteServiceImpl(
            NoteRepository NoteRepository,
            FileRepository fileRepository,
            AuthorityService authorityService,
            FileService fileService,
            StaticPathHelper staticPathHelper) {
        this.noteRepository = NoteRepository;
        this.fileRepository = fileRepository;
        this.authorityService = authorityService;
        this.fileService = fileService;
        this.staticPathHelper = staticPathHelper;
    }

    @Override
    public Map<Note, List<Files>> findNotesUnderNotebook(String notebookID) {
        var notes = noteRepository.findNotesByNotebookID(notebookID);
        Map<Note, List<Files>> files = new HashMap<>();
        for (var note : notes) {
            files.put(note, fileRepository.findFilesByNote(note));
        }
        return files;
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
    public boolean deleteNote(String noteID, String target){
        var note = authorityService.checkNoteAuthority(noteID);
        var files = fileRepository.findFilesByNote_NoteID(noteID);
        if (target != null) {
            logger.debug("transfer files from " + noteID + " to " + target);
            for (var file : files) {
                fileService.moveFile(file.getFileID(), target);
            }
        } else {
            logger.debug("delete files from " + noteID);
            for (var file : files) {
                fileService.deleteFile(file.getFileID());
            }
        }
        try {
            noteRepository.deleteNotesByNoteID(noteID);
            // 删除文件夹
            File file = new File(staticPathHelper.getStaticPath(), note.getSavingPath());
            return file.delete();
        } catch (Exception e) {
            logger.error("deleteNote error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Note> findAllNote(){
        return noteRepository.findAll();
    }

    @Override
    public int findNotesCountByNotebookID(String notebookID) {
        return noteRepository.findNoteCountByNotebookID(notebookID);
    }

    @Override
    public void renameNote(String noteID, String name) {
        var note = authorityService.checkNoteAuthority(noteID);
        note.setNoteName(name);
        noteRepository.save(note);
    }

    @Override
    public List<String> findNoteIDsByNotebookID(String noteBookID) {
        return noteRepository.findNoteIDsByNotebookID(noteBookID);
    }
}
