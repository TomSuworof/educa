package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseFullDTO;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExerciseEditorService extends ArticleEditorService<Exercise, ExerciseUploadRequest, ExerciseFullDTO> {
    public ExerciseEditorService(UserService userService, ArticleService articleService, TagService tagService) {
        super(userService, articleService, tagService);
    }

    @Override
    protected @NotNull Exercise newBlankEntity() {
        return new Exercise();
    }

    @Override
    protected void enrichFromRequest(Exercise exercise, ExerciseUploadRequest uploadRequest) {
        exercise.setTitle(uploadRequest.getTitle());
        exercise.setCustomUrl(uploadRequest.getCustomUrl());
        exercise.setSummary(uploadRequest.getSummary());
        exercise.setContent(uploadRequest.getContent());
        exercise.setSolution(uploadRequest.getSolution());
    }

    @NotNull
    @Override
    protected ExerciseFullDTO entityToDTO(Exercise exercise) {
        return new ExerciseFullDTO(exercise);
    }
}
