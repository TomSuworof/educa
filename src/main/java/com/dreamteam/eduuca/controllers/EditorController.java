package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.exceptions.ArticleFoundException;
import com.dreamteam.eduuca.services.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;

    @GetMapping("/editor")
    public String getEditorPage(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            Model model) {
        model.addAttribute("titleValue", title);
        model.addAttribute("contentValue", content);
        return "editor";
    }

    @GetMapping("/editor/load")
    public String publishArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String action,
            Model model) {
        try {
            editorService.loadArticle(title, content, action);
            return "redirect:/articles";
        } catch (ArticleFoundException | IllegalArgumentException e) {
            model.addAttribute("titleValue", title);
            model.addAttribute("contentValue", content);
            model.addAttribute("error", "Error occurred while publishing article");
            return "editor";
        }
    }
//
//    @GetMapping("/editor/save")
//    public String saveArticle(@RequestParam String title, @RequestParam String content, Model model) {
//        try {
//            editorService.saveArticle(title, content);
//            return "redirect:/articles";
//        } catch (ArticleFoundException e) {
//            model.addAttribute("titleValue", title);
//            model.addAttribute("contentValue", content);
//            model.addAttribute("error", "Error occurred while saving changes article");
//            return "editor";
//        }
//    }
}
