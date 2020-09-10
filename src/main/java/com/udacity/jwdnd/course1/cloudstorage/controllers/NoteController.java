package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;

    @PostMapping("/note")
    public String storeNote(Authentication authentication, Note note, Model model) {
        Integer rowsAffected;
        User currentLoggedInUser = userService.getUserByUserName(authentication.getName());
        String errorMessage = null;

        if(note.getNoteTitle().isEmpty() || note.getNoteDescription().isEmpty()) {
            errorMessage = "Note title or note description cannot be empty.";
        } else {
            if(note.getNoteId() == null) {
                note.setUserId(currentLoggedInUser.getUserId());
                rowsAffected = noteService.storeNewNote(note);

                if(rowsAffected == 0) {
                    errorMessage = "Unable to store note. Please try again later.";
                }
            } else {
                rowsAffected = noteService.updateNote(note);

                if(rowsAffected == 0) {
                    errorMessage = "Unable to update note. Please try again later.";
                }
            }
        }

        if(errorMessage == null) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("activeTab", "notes");

        return "result";
    }

    @GetMapping("/note-delete/{noteId}")
    public String deleteNote(@PathVariable(value="noteId") Integer noteId, Authentication authentication, Model model) {
        User currentLoggedInUser = userService.getUserByUserName(authentication.getName());
        String errorMessage = null;

        Integer rowsRemoved = noteService.removeNote(noteId, currentLoggedInUser.getUserId());

        if(rowsRemoved == 0) {
            errorMessage = "Something wrong happened. Failed to remove note.";
        }

        if(errorMessage == null) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("activeTab", "notes");

        return "result";
    }
}
