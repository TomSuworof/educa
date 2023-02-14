package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.entities.RoleEnum;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.UserDTO;
import com.dreamteam.eduuca.services.ExerciseService;
import com.dreamteam.eduuca.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ExerciseService exerciseService;
    private final UserService userService;

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<PageResponseDTO<UserDTO>> getUsersPaginated(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getUsersPaginated() called. Limit: {}, offset: {}", limit, offset);
        PageResponseDTO<UserDTO> response = userService.getUsersPaginated(limit, offset);
        log.trace("getUsersPaginated(). Response to send: {}", () -> response);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("getUsersPaginated(). Response contains all users. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("getUsersPaginated(). Response does not contain all users. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @PostMapping("/edit_user")
    @ResponseBody
    public ResponseEntity<UserDTO> changeRole(@RequestParam String action, @RequestParam UUID id) {
        log.debug("changeRole() called. Action: {}, ID: {}", action, id);
        UserDTO user = userService.changeRole(id, RoleEnum.getFromAction(action).getAsObject());
        log.trace("changeRole(). Result user: {}", () -> user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/exercises")
    @ResponseBody
    public ResponseEntity<PageResponseDTO<ExerciseDTO>> getExercisesPaginated(
            @RequestParam(required = false, defaultValue = "all") String state,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getExercisePaginated() called. State: {}, limit: {}, offset: {}", state, limit, offset);
        PageResponseDTO<ExerciseDTO> response = exerciseService.getPageWithExercisesByState(ExerciseState.getFromDescription(state), limit, offset);
        log.trace("getExercisePaginated(). Response to send: {}", () -> response);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("getExercisesPaginated(). Response contains all exercises. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("getExercisesPaginated(). Response does not contain all exercises. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }
}
