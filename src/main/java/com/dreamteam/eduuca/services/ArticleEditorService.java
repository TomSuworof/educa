package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.entities.ArticleState;
import com.dreamteam.eduuca.exceptions.ArticleFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleEditorService {
    private final ArticleService articleService;

    public void loadArticle(String title, String content, String action) throws ArticleFoundException {
        Article article = new Article();
        article.setFilename(title.replaceAll(" ", "_"));
        article.setTitle(title);
        article.setContent(content.getBytes());
        article.setState(switch (action) {
            case "Publish" -> ArticleState.ARTICLE_PUBLISHED;
            case "Save" -> ArticleState.ARTICLE_IN_EDITING;
            default -> throw new IllegalArgumentException();
        });
        articleService.saveArticle(article);
    }
}
