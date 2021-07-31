package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.entities.User;
import com.dreamteam.eduuca.exceptions.AnonymousUserException;
import com.dreamteam.eduuca.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String getStartPage() {
        try {
            User currentUser = userService.getUserFromContext();
            return "redirect:/articles";
        } catch (AnonymousUserException e) {
            return "index";
        }
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/terms_of_use")
    public String getTerms() {
        return "terms_of_use";
    }

    @GetMapping("/favicon.ico")
    public String getFavicon() {
        return "forward:/public/img/favicon.png";
    }
}