package com.example.SUSTechNote.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.SUSTechNote.api.FileRepository;
import com.example.SUSTechNote.api.NoteRepository;
import com.example.SUSTechNote.api.NotebookRepository;
import com.example.SUSTechNote.entity.Files;
import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.entity.Notebook;
import com.example.SUSTechNote.exception.FileNotExistException;
import com.example.SUSTechNote.exception.ModifyNotAuthoredException;
import com.example.SUSTechNote.exception.NoteNotExistException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthorityService {
    private final FileRepository fileRepository;
    private final NoteRepository noteRepository;
    private final NotebookRepository notebookRepository;

    public AuthorityService(
            FileRepository fileRepository,
            NoteRepository noteRepository,
            NotebookRepository notebookRepository) {
        this.fileRepository = fileRepository;
        this.noteRepository = noteRepository;
        this.notebookRepository = notebookRepository;
    }

    public Files checkFileAuthority(String fileID)
            throws FileNotExistException, ModifyNotAuthoredException {
        Files file = fileRepository.findFilesByFileID(fileID);
        if (file == null) {
            throw new FileNotExistException("file not found");
        }
        checkNoteAuthority(file.getNote());
        return file;
    }

    /**
     * Check whether the user has the authority to operate the note
     * @param noteID the note ID
     * @throws NoteNotExistException if the note does not exist
     * @throws ModifyNotAuthoredException if the user has no authority to modify the note
     * @return the note been authorized
     */
    public Note checkNoteAuthority(String noteID)
            throws NoteNotExistException, ModifyNotAuthoredException {
        Note note = noteRepository.findNoteByNoteID(noteID);
        if (note == null) {
            throw new NoteNotExistException("note not found");
        }
        checkNoteAuthority(note);
        return note;
    }
    public void checkNoteAuthority(Note note) {
        String notebookID = note.getNotebookID();
        checkNotebookAuthority(notebookID);
    }

    /**
     * Check whether the user has the authority to operate the notebook
     * @param notebookID the notebook ID
     * @throws NoteNotExistException if the notebook does not exist
     * @throws ModifyNotAuthoredException if the user has no authority to modify the notebook
     * @return the notebook been authorized
     */
    public Notebook checkNotebookAuthority(String notebookID) {
        Notebook notebook = notebookRepository.findNotebookByNotebookID(notebookID);
        if (notebook == null) {
            throw new NoteNotExistException("notebook not found");
        }
        if (!Objects.equals(notebook.getAuthorID(), StpUtil.getLoginIdAsInt())) {
            throw new ModifyNotAuthoredException("not authorized to modify this notebook");
        }
        return notebook;
    }
}
