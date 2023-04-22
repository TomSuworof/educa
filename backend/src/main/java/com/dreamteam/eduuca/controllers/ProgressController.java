package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.common.ProgressDTO;
import com.dreamteam.eduuca.services.progress.ProgressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ProgressDTO> saveProgress(@RequestBody ProgressDTO progressDTO, Authentication auth) {
        log.debug("saveProgress() called. Progress: {}", progressDTO);
        progressService.saveProgressEntity(progressDTO, auth);
        log.trace("saveProgress(). Progress saved. Sending DTO back with OK");
        return ResponseEntity.ok().body(progressDTO);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ProgressDTO> getProgress(@RequestParam UUID userID, @RequestParam UUID articleID) {
        log.debug("getProgress() called. User ID={}, article ID={}", userID, articleID);
        ProgressDTO progressDTO = progressService.getProgress(userID, articleID);
        log.trace("getProgress(). Response to send: {}", progressDTO);
        return ResponseEntity.ok().body(progressDTO);
    }
}
