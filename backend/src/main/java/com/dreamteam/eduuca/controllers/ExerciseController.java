package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.services.ExerciseEditorService;
import com.dreamteam.eduuca.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final ExerciseEditorService exerciseEditorService;

    @GetMapping("/")
    public ResponseEntity<PageResponseDTO<ExerciseDTO>> getExercisesPaginated(@RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<ExerciseDTO> response = exerciseService.getPageWithExercisesByState(ExerciseState.PUBLISHED, limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable UUID id) {
        Exercise exercise = exerciseService.getExerciseById(id);

        return ResponseEntity.ok().body(new ExerciseDTO(exercise));
    }

    @PostMapping("/")
    public ResponseEntity<ExerciseDTO> publishExercise(@RequestBody ExerciseUploadRequest exercise, @RequestParam String action) {
        ExerciseDTO exerciseDTO = exerciseEditorService.loadExercise(exercise, action);
        return ResponseEntity.ok().body(exerciseDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteExercise(@PathVariable UUID id) {
        exerciseService.deleteExerciseById(id);
    }
}
