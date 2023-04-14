package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Lecture;
import com.dreamteam.eduuca.payload.response.LectureDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LectureQueryService extends ArticleQueryService<Lecture, LectureDTO> {
    public LectureQueryService(UserService userService, TagService tagService, ArticleRepository<Lecture> articleRepository) {
        super(userService, tagService, articleRepository);
    }

    @Override
    protected @Nullable LectureDTO parseToDTO(@NotNull Lecture lecture) {
        return new LectureDTO(lecture);
    }
}
