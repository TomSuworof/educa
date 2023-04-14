package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.entities.ArticleState;
import com.dreamteam.eduuca.entities.User;
import com.dreamteam.eduuca.payload.request.ArticleUploadRequest;
import com.dreamteam.eduuca.payload.response.ArticleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public abstract class ArticleEditorService<E extends Article, R extends ArticleUploadRequest, DTO extends ArticleDTO> {
    private final UserService userService;
    private final ArticleService articleService;
    private final TagService tagService;

    public DTO upload(R uploadRequest, ArticleState articleState, Authentication auth) {
        log.debug("upload() called. Article upload request: {}, exercise state: {}", () -> uploadRequest, () -> articleState);
        E article = newBlankEntity();

        if (uploadRequest.getId() != null) {
            log.trace("upload(). Article upload request contains ID={}, setting this ID to new article", uploadRequest.getId());
            article.setId(uploadRequest.getId());
        } else {
            log.trace("upload(). Article upload request does not contains ID, setting new random");
            article.setId(UUID.randomUUID());
        }

        if (uploadRequest.getCustomUrl() == null) {
            log.trace("upload(). Article upload request does not contain custom URL, setting title as URL");
            uploadRequest.setCustomUrl(uploadRequest.getTitle());
        }

        User author = userService.getUserFromAuthentication(auth);
        log.trace("uploadExercise(). Exercise author: {}", () -> author);
        article.setAuthor(author);

        enrichFromRequest(article, uploadRequest);

        article.setState(articleState);
        article.setTags(tagService.saveTags(uploadRequest.getTags()));

        log.trace("upload(). Result article to save: {}", () -> article);
        articleService.saveArticle(article);
        log.trace("upload(). Exercise successfully saved. Exercise: {}", () -> article);

        return entityToDTO(article);
    }

    protected abstract @NotNull E newBlankEntity();

    protected abstract void enrichFromRequest(E articleToEnrich, R uploadRequest);

    protected abstract @NotNull DTO entityToDTO(E article);
}
