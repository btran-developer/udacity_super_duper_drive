package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    private UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getSignUpView(Authentication authentication) {
        if(authentication != null) {
            return "redirect:/";
        }

        return "signup";
    }

    @PostMapping("")
    public String signUp(@ModelAttribute User user, Model model) {
        String signupError = null;

        if(!userService.isUserNameAvailable(user.getUsername())) {
            signupError = "This username is already taken";
        }

        if(signupError == null) {
            int rowsAdded = userService.createUser(user);

            if(rowsAdded == 0) {
                signupError = "Something went wrong in the process of creating the account";
            }
        }

        if(signupError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }
}
