package com.dreamteam.eduuca.services.article.editor;

import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import com.dreamteam.eduuca.payload.common.QuestionDTO;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseFullDTO;
import com.dreamteam.eduuca.services.QuestionService;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.services.article.ArticleSaveService;
import com.dreamteam.eduuca.utils.ExerciseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Log4j2
@Service
public class ExerciseEditorService extends ArticleEditorService<Exercise, ExerciseUploadRequest, ExerciseFullDTO> {
    private final QuestionService questionService;

    public ExerciseEditorService(UserService userService, ArticleSaveService articleSaveService, TagService tagService, QuestionService questionService) {
        super(userService, articleSaveService, tagService);
        this.questionService = questionService;
    }

    @Override
    protected @NotNull Exercise newBlankEntity() {
        return new Exercise();
    }

    @Override
    protected void enrichFromRequest(@NotNull Exercise exercise, @NotNull ExerciseUploadRequest uploadRequest) {
        exercise.setTitle(uploadRequest.getTitle());
        exercise.setCustomUrl(uploadRequest.getCustomUrl());
        exercise.setSummary(uploadRequest.getSummary());
        exercise.setContent(uploadRequest.getContent());
        exercise.setSolution(uploadRequest.getSolution());
    }

    @Override
    protected @NotNull ExerciseFullDTO entityToDTO(@NotNull Exercise exercise) {
        return new ExerciseFullDTO(exercise);
    }

    @Override
    protected void postProcess(@NotNull Exercise exercise, @NotNull Authentication auth) {
        extractQuestions(exercise.getSolution(), auth);
    }

    private void extractQuestions(String solution, Authentication auth) {
        Arrays.stream(solution.split("<question>"))
                .filter(ExerciseUtils::isQuestion)
                .forEach(solutionPart -> {
                    try {
                        QuestionDTO questionDTO = ExerciseUtils.toQuestion(solutionPart);
                        questionService.saveQuestion(questionDTO, auth);
                    } catch (JsonProcessingException ignored) {
                    }
                });
    }
}
