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
    public String getArticles(Model model) {
        try {
            User currentUser = userService.getUserFromContext();
            model.addAttribute("isEditor", userService.isUser(currentUser, "editor"));
        } catch (AnonymousUserException ignored) {
        }
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles";
    }

    @PostMapping("/articles/load")
    public String loadFile(@RequestParam String title, @RequestParam MultipartFile loadedFile, Model model) {
        try {
            articleService.saveFile(title, loadedFile);
            return "redirect:/articles";
        } catch (IOException e) {
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

    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable Long id, Model model) {
        try {
            Article article = articleService.getArticleById(id);
            return "redirect: /editor?" +
                    "title=" + article.getTitle() +
                    ";content=" + new String(article.getContent());
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot edit article");
            return "articles";
        }
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable Long id, Model model) {
        try {
            articleService.deleteArticle(id);
            return "redirect: /articles";
        } catch (ArticleNotFoundException e) {
            e.printStackTrace();
            model.addAttribute("error", "Cannot edit article");
            return "articles";
        }
    }
}
