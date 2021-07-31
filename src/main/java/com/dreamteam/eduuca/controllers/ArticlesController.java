package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.entities.User;
import com.dreamteam.eduuca.exceptions.AnonymousUserException;
import com.dreamteam.eduuca.exceptions.ArticleNotFoundException;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.services.ArticleService;
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
    public String getArticles(@RequestParam(required = false) String type, Model model) {
        try {
            User currentUser = userService.getUserFromContext();
            if (userService.isUser(currentUser, "article_editor")) {
                model.addAttribute("isEditor", true);
                model.addAttribute("articles", articleService.getArticlesByType(type));
                return "articles";
            }
        } catch (AnonymousUserException ignored) {
        }
        model.addAttribute("articles", articleService.getArticlesByType("published"));
        return "articles";
    }

    @PostMapping("/articles/load")
    public String loadFile(@RequestParam String title, @RequestParam MultipartFile loadedFile, Model model) {
        try {
            articleService.saveFile(title, loadedFile);
            return "redirect:/articles";
        } catch (IOException e) {
            e.printStackTrace();
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

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id, Model model) {
        try {
            articleService.deleteArticle(id);
            return "redirect:/articles";
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot delete article");
            return "articles";
        }
    }
}
