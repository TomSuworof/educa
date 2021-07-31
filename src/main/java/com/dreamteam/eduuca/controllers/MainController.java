package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.entities.User;
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
        User currentUser = userService.getUserFromContext();
        if (currentUser != null) {
            return "redirect:/articles";
        }
        return "index";
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