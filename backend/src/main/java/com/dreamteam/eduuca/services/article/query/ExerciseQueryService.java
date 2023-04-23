package com.dreamteam.eduuca.services.article.query;

import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import com.dreamteam.eduuca.payload.common.QuestionDTO;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseShortDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.ThemeService;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.utils.ExerciseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Log4j2
@Service
public class ExerciseQueryService extends ArticleQueryService<Exercise, ExerciseShortDTO> {
    public ExerciseQueryService(UserService userService, TagService tagService, ThemeService themeService, ArticleRepository<Exercise> articleRepository) {
        super(userService, tagService, themeService, articleRepository);
    }

    @Nullable
    @Override
    protected ExerciseShortDTO parseToDTO(@NotNull Exercise exercise) {
        return new ExerciseShortDTO(exercise);
    }

    public @NotNull Exercise clearAnswers(@NotNull Exercise exercise) {
        StringBuilder exerciseSolutionCleared = new StringBuilder();
        String exerciseSolution = exercise.getSolution();

        Arrays.stream(exerciseSolution.split("<question>"))
                .forEach(solutionPart -> {
                    if (ExerciseUtils.isQuestion(solutionPart)) {
                        try {
                            QuestionDTO questionDTO = ExerciseUtils.toQuestion(solutionPart);
                            exerciseSolutionCleared.append(ExerciseUtils.toString(new QuestionDTO(
                                    questionDTO.id(),
                                    questionDTO.exerciseId(),
                                    null,
                                    questionDTO.remark(),
                                    questionDTO.hint()
                            )));
                        } catch (JsonProcessingException ignored) {
                        }
                    } else {
                        exerciseSolutionCleared.append(solutionPart);
                    }
                });

        exercise.setSolution(exerciseSolutionCleared.toString());
        return exercise;
    }
}
