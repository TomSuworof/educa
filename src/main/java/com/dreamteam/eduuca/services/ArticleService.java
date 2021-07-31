package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.exceptions.ArticleFoundException;
import com.dreamteam.eduuca.exceptions.ArticleNotFoundException;
import com.dreamteam.eduuca.exceptions.IllegalArticleExtensionException;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    private boolean titleExists(String title) {
        return articleRepository.findArticleByTitle(title).isPresent();
    }

    private boolean isMarkdown(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).matches("(.*).md");
        // return Objects.equals(file.getContentType(), "md");
    }

    public void saveFile(String title, MultipartFile file) throws IOException {
        Article article = new Article(title, file);
        if (!isMarkdown(file)) {
            throw new IllegalArticleExtensionException();
        }
        saveArticle(article);
    }

    public void saveArticle(Article article) throws ArticleFoundException {
        if (titleExists(article.getTitle())) {
            throw new ArticleFoundException();
        }
        article.setId((long) article.hashCode());
        articleRepository.save(article);
    }

    public void deleteArticle(Long articleId) throws ArticleNotFoundException {
        if (articleRepository.findById(articleId).isPresent()) {
            articleRepository.deleteById(articleId);
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public Article getArticleById(Long id) throws ArticleNotFoundException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            return articleOptional.get();
        } else {
            throw new ArticleNotFoundException();
        }
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
}