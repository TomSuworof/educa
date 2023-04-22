package com.dreamteam.eduuca.services.article.query;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseShortDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExerciseQueryService extends ArticleQueryService<Exercise, ExerciseShortDTO> {
    public ExerciseQueryService(UserService userService, TagService tagService, ArticleRepository<Exercise> articleRepository) {
        super(userService, tagService, articleRepository);
    }

    @Nullable
    @Override
    protected ExerciseShortDTO parseToDTO(@NotNull Exercise exercise) {
        return new ExerciseShortDTO(exercise);
    }
}
