package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExerciseEditorService {
    private final ExerciseService exerciseService;

    public ExerciseDTO uploadExercise(ExerciseUploadRequest exerciseUploadRequest, ExerciseState exerciseState) {
        log.debug("uploadExercise() called. Exercise upload request: {}, exercise state: {}", () -> exerciseUploadRequest, () -> exerciseState);
        Exercise exercise = new Exercise();
        log.trace("uploadExercise(). Blank new exercise: {}", () -> exercise);

        if (exerciseUploadRequest.getId() != null) {
            log.trace("uploadExercise(). Exercise upload request contains ID={}, setting this ID to new Exercise", exerciseUploadRequest.getId());
            exercise.setId(exerciseUploadRequest.getId());
        } else {
            log.trace("uploadExercise(). Exercise upload request does not contains ID, setting new random");
            exercise.setId(UUID.randomUUID());
        }

        if (exerciseUploadRequest.getCustomUrl() == null) {
            log.trace("uploadExercise(). Exercise upload request does not contain custom URL, setting title as URL");
            exerciseUploadRequest.setCustomUrl(exerciseUploadRequest.getTitle());
        }

        exercise.setTitle(exerciseUploadRequest.getTitle());
        exercise.setCustomUrl(exerciseUploadRequest.getCustomUrl());
        exercise.setContent(exerciseUploadRequest.getContent());
        exercise.setSolution(exerciseUploadRequest.getSolution());
        exercise.setState(exerciseState);

        log.trace("uploadExercise(). Result exercise to save: {}", () -> exercise);
        exerciseService.saveExercise(exercise);
        log.trace("uploadExercise(). Exercise successfully saved. Exercise: {}", () -> exercise);

        return new ExerciseDTO(exercise);
    }
}
