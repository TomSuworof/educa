package com.dreamteam.eduuca.services.article.query;

import com.dreamteam.eduuca.entities.Lecture;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureShortDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LectureQueryService extends ArticleQueryService<Lecture, LectureShortDTO> {
    public LectureQueryService(UserService userService, TagService tagService, ArticleRepository<Lecture> articleRepository) {
        super(userService, tagService, articleRepository);
    }

    @Override
    protected @Nullable LectureShortDTO parseToDTO(@NotNull Lecture lecture) {
        return new LectureShortDTO(lecture);
    }
}
