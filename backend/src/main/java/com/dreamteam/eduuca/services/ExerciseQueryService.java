package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExerciseQueryService extends ArticleQueryService<Exercise, ExerciseDTO> {
    public ExerciseQueryService(UserService userService, TagService tagService, ArticleRepository<Exercise> articleRepository) {
        super(userService, tagService, articleRepository);
    }

    @Nullable
    @Override
    protected ExerciseDTO parseToDTO(@NotNull Exercise exercise) {
        return new ExerciseDTO(exercise);
    }
}