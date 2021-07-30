package com.dreamteam.educa.services;

import com.dreamteam.educa.entities.Article;
import com.dreamteam.educa.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public boolean loadFile(String title, MultipartFile file) {
        try {
            Article article = new Article(title, file);
            return !titleExists(title) && isMarkdown(file) && saveArticle(article);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean titleExists(String title) {
        return articleRepository.findArticleByTitle(title).isPresent();
    }

    private boolean isMarkdown(MultipartFile file) {
        return file.getOriginalFilename().matches("(.*).md");
        // return Objects.equals(file.getContentType(), "md");
    }

    private boolean saveArticle(Article article) {
        try {
            article.setId((long) article.hashCode());
            articleRepository.save(article);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteArticle(Long id) {
        Optional<Article> articleForDeleting = articleRepository.findById(id);
        if (articleForDeleting.isPresent()) {
            return deleteArticle(articleForDeleting.get());
        }
        return false;

    }

    private boolean deleteArticle(Article article) {
        try {
            articleRepository.delete(article);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Article getArticleById(Long id) throws FileNotFoundException {
        Optional<Article> articleOptional = articleRepository.findById(id);
        if (articleOptional.isPresent()) {
            return articleOptional.get();
        } else {
            throw new FileNotFoundException();
        }
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public boolean createArticle(String title, String content) {
        Article article = new Article();
        article.setFilename(title.replaceAll(" ", "_"));
        article.setTitle(title);
        article.setContent(content.getBytes());
        return saveArticle(article);
    }
}