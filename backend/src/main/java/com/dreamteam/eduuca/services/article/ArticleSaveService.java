package com.dreamteam.eduuca.services.article;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ArticleSaveService {
    private final ArticleRepository<Article> articleRepository;

    public void saveArticle(@NotNull Article article) {
        log.debug("saveArticle() called. Article: {}", () -> article);

        switch (article.getState()) {
            case PUBLISHED -> savePublished(article);
            case IN_EDITING -> saveDraft(article);
            default -> {
                log.warn("saveArticle(). Article have incorrect state. Will throw exception");
                throw new IllegalStateException("Incorrect action");
            }
        }
    }

    private void saveDraft(@NotNull Article newArticle) {
        log.debug("saveDraft() called. New article: {}", () -> newArticle);
        Optional<Article> oldArticleOpt = articleRepository.findById(newArticle.getId());

        if (oldArticleOpt.isEmpty()) {
            log.trace("saveDraft(). Old article with same ID={} does not exists. Going to create new", newArticle.getId());
            createDraft(newArticle);
        } else {
            log.trace("saveDraft(). Old article with same ID={} exists. Going to edit", newArticle.getId());
            editDraft(newArticle, oldArticleOpt.get());
        }
    }

    private void createDraft(@NotNull Article newArticle) {
        log.debug("createDraft() called. New article: {}", () -> newArticle);

        if (articleRepository.findByCustomUrl(newArticle.getCustomUrl()).isPresent()) {
            log.warn("createDraft(). Article with same URL '{}' already exists. Will throw exception", newArticle.getCustomUrl());
            throw new IllegalStateException(String.format("Article with this URL %s already exists", newArticle.getCustomUrl()));
        }

        saveArticleToRepo(newArticle, OffsetDateTime.now());
    }

    private void editDraft(@NotNull Article newArticle, @NotNull Article oldArticle) {
        log.debug("editDraft() called. New article: {}, old article: {}", () -> newArticle, () -> oldArticle);

        if (!newArticle.getAuthor().equals(oldArticle.getAuthor())) {
            throw new SecurityException("Current user does not have rights to edit article");
        }

        saveArticleToRepo(newArticle, OffsetDateTime.now());
    }

    private void savePublished(@NotNull Article newArticle) {
        log.debug("savePublished() called. New Article: {}", () -> newArticle);

        Optional<Article> oldArticleOpt = articleRepository.findById(newArticle.getId());

        if (oldArticleOpt.isEmpty()) {
            log.trace("savePublished(). Old Article with same ID={} does not exists, Going to create new", newArticle.getId());
            createPublished(newArticle);
        } else {
            log.trace("savePublished(). Old Article with same ID={} exists. Going to edit", newArticle.getId());
            editPublished(newArticle, oldArticleOpt.get());
        }
    }

    private void createPublished(@NotNull Article newArticle) {
        log.debug("createPublished() called. New article: {}", () -> newArticle);
        if (articleRepository.findByCustomUrl(newArticle.getCustomUrl()).isPresent()) {
            log.warn("createPublished(). Article with same URL '{}' already exists. Will throw exception", newArticle.getCustomUrl());
            throw new IllegalStateException(String.format("Article with this URL %s already exists", newArticle.getCustomUrl()));
        }

        saveArticleToRepo(newArticle, OffsetDateTime.now());
    }

    private void editPublished(@NotNull Article newArticle, @NotNull Article oldArticle) {
        log.debug("editPublished() called. New article: {}, old article: {}", () -> newArticle, () -> oldArticle);

        if (!newArticle.getAuthor().equals(oldArticle.getAuthor())) {
            throw new SecurityException("Current user does not have rights to edit article");
        }

        saveArticleToRepo(newArticle, oldArticle.getPublicationDate());
    }

    private void saveArticleToRepo(@NotNull Article article, OffsetDateTime time) {
        log.debug("saveArticleToRepo() called. Article: {}", () -> article);
        article.setPublicationDate(time);
        Article saved = articleRepository.save(article);
        log.trace("saveArticleToRepo(). Article successfully saved: {}", () -> saved);
    }
}
