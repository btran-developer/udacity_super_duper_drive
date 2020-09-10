package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getUserNotes(String userName) {
        return noteMapper.getNotesByUserName(userName);
    }

    public int storeNewNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public int updateNote(Note note) { return noteMapper.updateNote(note); }

    public int removeNote(Integer noteId, Integer userId) { return noteMapper.deleteNote(noteId, userId); }

    public int removeAllUserNotes(Integer userId) { return noteMapper.deleteUserNotes(userId); }
}
