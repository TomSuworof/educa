package com.dreamteam.educa.controllers;

import com.dreamteam.educa.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EditorController {
    private final ArticleService articleService;

    @GetMapping("/editor")
    public String getEditorPage() {
        return "editor";
    }

    @GetMapping("/editor/load")
    public String loadArticle(@RequestParam String title, @RequestParam String content, Model model) {
        if (articleService.createArticle(title, content)) {
            return "redirect:/articles";
        } else {
            model.addAttribute("titleValue", title);
            model.addAttribute("contentValue", content);
            model.addAttribute("error", "Error occurred while saving article");
            return "editor";
        }
    }
}
