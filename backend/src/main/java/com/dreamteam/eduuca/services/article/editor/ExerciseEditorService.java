package com.dreamteam.eduuca.services.article.editor;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseFullDTO;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.services.article.ArticleSaveService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExerciseEditorService extends ArticleEditorService<Exercise, ExerciseUploadRequest, ExerciseFullDTO> {
    public ExerciseEditorService(UserService userService, ArticleSaveService articleSaveService, TagService tagService) {
        super(userService, articleSaveService, tagService);
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

    @NotNull
    @Override
    protected ExerciseFullDTO entityToDTO(@NotNull Exercise exercise) {
        return new ExerciseFullDTO(exercise);
    }
}
