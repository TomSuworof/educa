package com.dreamteam.eduuca.services.article.query;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ArticleQueryService {
    private final ExerciseQueryService exerciseQueryService;
    private final LectureQueryService lectureQueryService;

    public Optional<Article> getArticle(@NotNull UUID articleID, @NotNull Authentication auth) {
        log.debug("getArticle() called. Article ID={}", articleID);
        try {
            Exercise exercise = exerciseQueryService.getById(articleID, auth);
            log.trace("getArticle(). Exercise with ID={} found", articleID);
            return Optional.of(exercise);
        } catch (EntityNotFoundException ignored) {
            log.warn("getArticle(). Exercise with ID={} not found", articleID);
        }

        try {
            Lecture lecture = lectureQueryService.getById(articleID, auth);
            log.trace("getArticle(). Lecture with ID={} found", articleID);
            return Optional.of(lecture);
        } catch (EntityNotFoundException ignored) {
            log.warn("getArticle(). Lecture with ID={} not found", articleID);
        }

        return Optional.empty();
    }
}
