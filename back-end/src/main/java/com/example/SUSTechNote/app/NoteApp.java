package com.example.SUSTechNote.app;

import com.example.SUSTechNote.entity.Note;
import com.example.SUSTechNote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NoteApp {
    @Autowired
    NoteService noteService;

    @PostMapping("/addNote")
    public int addnote(String noteID,String content,Integer isPublic ){
        return noteService.addNote(noteID,isPublic);
    };

    @PostMapping("/updatenote")
    public int updatenote(Note note){
        return noteService.updateNote(note);
    };


    @GetMapping("/deletenote")
    public int deletenote(String noteID){
        return noteService.deleteNote(noteID);
    };

    @GetMapping("/findAllnote")
    public List<Note> findAllnote(){
        return noteService.findAllNote();
    };
}
