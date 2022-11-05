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

    public ExerciseDTO loadExercise(ExerciseUploadRequest exerciseUploadRequest, String action) {
        Exercise exercise = new Exercise();
        exercise.setTitle(exerciseUploadRequest.getTitle());
        exercise.setContent(exerciseUploadRequest.getContent());
        exercise.setSolution(exerciseUploadRequest.getSolution());
        exercise.setState(switch (action) {
            case "Publish" -> ExerciseState.PUBLISHED;
            case "Save" -> ExerciseState.IN_EDITING;
            default -> throw new IllegalArgumentException();
        });
        exerciseService.saveExercise(exercise);
        return new ExerciseDTO(exercise);
    }
}
