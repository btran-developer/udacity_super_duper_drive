package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {

    @Autowired
    CredentialService credentialService;
    @Autowired
    UserService userService;

    @PostMapping("/credential")
    public String storeCredential(Authentication authentication, Credential credential, Model model) {
        Integer rowsAffected;
        User currentLoggedInUser = userService.getUserByUserName(authentication.getName());
        String errorMessage = null;

        if(credential.getUrl().isEmpty() || credential.getUsername().isEmpty() || credential.getPassword().isEmpty()) {
            errorMessage = "Credential url, username, and password cannot be empty.";
        } else {
            if(credential.getCredentialId() == null) {
                credential.setUserId(currentLoggedInUser.getUserId());
                rowsAffected = credentialService.storeNewCredential(credential);

                if(rowsAffected == 0) {
                    errorMessage = "Unable to store credential. Please try again later.";
                }
            } else {
                rowsAffected = credentialService.updateCredential(credential);

                if(rowsAffected == 0) {
                    errorMessage = "Unable to update credential. Please try again later.";
                }
            }
        }

        if(errorMessage == null) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("activeTab", "credentials");

        return "result";
    }

    @GetMapping("/credential-delete/{credentialId}")
    public String removeCredential(@PathVariable(value="credentialId") Integer credentialId, Authentication authentication, Model model) {
        User currentLoggedInUser = userService.getUserByUserName(authentication.getName());
        String errorMessage = null;

        Integer rowsRemoved = credentialService.removeCredential(credentialId, currentLoggedInUser.getUserId());

        if(rowsRemoved == 0) {
            errorMessage = "Something wrong happened. Failed to remove credential.";
        }

        if(errorMessage == null) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("activeTab", "credentials");

        return "result";
    }
}
