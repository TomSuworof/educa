package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.config.ControllerUtils;
import com.dreamteam.eduuca.entities.ArticleState;
import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.payload.request.ExerciseUploadRequest;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseFullDTO;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseShortDTO;
import com.dreamteam.eduuca.services.ExerciseEditorService;
import com.dreamteam.eduuca.services.ExerciseQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseQueryService exerciseQueryService;
    private final ExerciseEditorService exerciseEditorService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<PageResponseDTO<ExerciseShortDTO>> getExercisesPaginated(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getExercisesPaginated() called. Limit={}, offset={}", limit, offset);
        PageResponseDTO<ExerciseShortDTO> response = exerciseQueryService.getPageByState(ArticleState.PUBLISHED, limit, offset);
        log.trace("getExercisesPaginated(). Response to send: {}", () -> response);
        return ControllerUtils.processPartialResponse(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ExerciseFullDTO> getExercise(@PathVariable UUID id, Authentication auth) {
        log.debug("getExercise() called. ID to search: {}", id);
        Exercise exercise = exerciseQueryService.getById(id, auth);
        log.trace("getExercise(). Exercise to return: {}", () -> exercise);
        return ResponseEntity.ok().body(new ExerciseFullDTO(exercise));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<ExerciseFullDTO> uploadExercise(@RequestBody ExerciseUploadRequest exercise, @RequestParam String action, Authentication auth) {
        log.debug("uploadExercise() called. Exercise: {}, action: {}", exercise, action);
        ExerciseFullDTO exerciseDTO = exerciseEditorService.upload(exercise, ArticleState.getFromAction(action), auth);
        log.trace("uploadExercise(). Result exercise DTO: {}", () -> exerciseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseDTO);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteExercise(@PathVariable UUID id, Authentication auth) {
        log.debug("deleteExercise() called. ID: {}", id);
        exerciseQueryService.deleteById(id, auth);
    }
}
