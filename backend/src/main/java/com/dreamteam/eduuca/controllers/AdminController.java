package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.entities.RoleEnum;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.UserDTO;
import com.dreamteam.eduuca.services.ExerciseService;
import com.dreamteam.eduuca.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

//@CrossOrigin(origins = "*")
@Controller
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ExerciseService exerciseService;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<PageResponseDTO<UserDTO>> getUsersPaginated(@RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<UserDTO> response = userService.getUsersPaginated(limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }

    @PostMapping("/edit_user")
    public ResponseEntity<UserDTO> changeRole(@RequestParam String action, @RequestParam UUID id) {
        UserDTO user = userService.changeRole(id, RoleEnum.getFromAction(action).getAsObject());
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/exercises")
    public ResponseEntity<PageResponseDTO<ExerciseDTO>> getExercisesPaginated(@RequestParam String state, @RequestParam Integer limit, @RequestParam Integer offset) {
        PageResponseDTO<ExerciseDTO> response = exerciseService.getPageWithExercisesByState(ExerciseState.getFromDescription(state), limit, offset);

        if (!response.isHasBefore() && !response.isHasAfter()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }
}
