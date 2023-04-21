package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.payload.response.article.ArticleShortDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ExerciseQueryService exerciseQueryService;
    private final LectureQueryService lectureQueryService;

    public List<ArticleShortDTO> search(String query) {
        log.debug("search() called. Query: {}", () -> query);
        List<ArticleShortDTO> articles = new LinkedList<>();

        articles.addAll(exerciseQueryService.search(query));
        articles.addAll(lectureQueryService.search(query));

        log.trace("search(). Search results count: {}", articles::size);

        return articles;
    }
}
