package com.dreamteam.educa.controllers;

import com.dreamteam.educa.entities.User;
import com.dreamteam.educa.services.UserService;
import com.dreamteam.educa.entities.Article;
import com.dreamteam.educa.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ArticlesController {
    private final UserService userService;
    private final ArticleService articleService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        User currentUser = userService.getUserFromContext();
        model.addAttribute("isEditor", userService.isUser(currentUser, "editor"));
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles";
    }

    @PostMapping("/articles/load")
    public String loadFile(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "file") MultipartFile loadedFile,
            Model model) {
        if (articleService.loadFile(title, loadedFile)) {
            return "redirect:/articles";
        } else {
            model.addAttribute("error", "Error loading article");
            return "articles";
        }
    }

    @GetMapping("/articles/{id}")
    public String deleteFile(@PathVariable Long id, Model model) {
        try {
            Article article = articleService.getArticleById(id);
            model.addAttribute("title", article.getTitle());
            model.addAttribute("content", article.makeHTML());
            return "article";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot open article");
            return "articles";
        }
    }
}
