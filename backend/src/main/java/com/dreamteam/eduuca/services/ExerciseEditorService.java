package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseEditorService {
    private final ExerciseService exerciseService;

    public ExerciseDTO uploadExercise(ExerciseUploadRequest exerciseUploadRequest, ExerciseState exerciseState) {
        Exercise exercise = new Exercise();

        if (exerciseUploadRequest.getId() != null) {
            exercise.setId(exerciseUploadRequest.getId());
        }

        if (exerciseUploadRequest.getCustomUrl() == null) {
            exerciseUploadRequest.setCustomUrl(exerciseUploadRequest.getTitle());
        }

        exercise.setTitle(exerciseUploadRequest.getTitle());
        exercise.setCustomUrl(exerciseUploadRequest.getCustomUrl());
        exercise.setContent(exerciseUploadRequest.getContent());
        exercise.setSolution(exerciseUploadRequest.getSolution());
        exercise.setState(exerciseState);
        exerciseService.saveExercise(exercise);
        return new ExerciseDTO(exercise);
    }
}
