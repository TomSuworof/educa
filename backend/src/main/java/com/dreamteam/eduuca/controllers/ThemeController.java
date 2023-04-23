package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.common.ThemeDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.article.exercise.ExerciseShortDTO;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureShortDTO;
import com.dreamteam.eduuca.services.ThemeService;
import com.dreamteam.eduuca.services.article.query.ExerciseQueryService;
import com.dreamteam.eduuca.services.article.query.LectureQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    private final ExerciseQueryService exerciseQueryService;
    private final LectureQueryService lectureQueryService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ThemeDTO> getTheme(@RequestParam UUID themeID) {
        log.debug("getTheme() called. Theme ID={}", themeID);
        ThemeDTO response = new ThemeDTO(themeService.getTheme(themeID));
        log.trace("getTheme(). Response to send: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get_children")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<ThemeDTO>> getChildren(@RequestParam UUID themeID) {
        log.debug("getChildren() called. Theme ID={}", themeID);
        List<ThemeDTO> response = themeService.getChildrenThemes(themeID);
        log.trace("getChildren(). Response to send: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ThemeDTO> saveTheme(@RequestBody ThemeDTO themeDTO) {
        log.debug("saveTheme() called. Theme: {}", themeDTO);
        themeService.saveTheme(themeDTO);
        log.trace("saveTheme(). Theme was saved, will return DTO back with OK");
        return ResponseEntity.ok().body(themeDTO);
    }

    @GetMapping("/get_exercises")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<PageResponseDTO<ExerciseShortDTO>> getExercisesByTheme(
            @RequestParam UUID themeID,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getExercisesByTheme() called. Theme ID: {}, limit: {}, offset: {}", themeID, limit, offset);
        PageResponseDTO<ExerciseShortDTO> response = exerciseQueryService.getPageByTheme(themeID, limit, offset);
        log.trace("getExercisesByTheme(). Response: {}", () -> response);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get_lectures")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<PageResponseDTO<LectureShortDTO>> getLecturesByTheme(
            @RequestParam UUID themeID,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getLecturesByTheme() called. Theme ID: {}, limit: {}, offset: {}", themeID, limit, offset);
        PageResponseDTO<LectureShortDTO> response = lectureQueryService.getPageByTheme(themeID, limit, offset);
        log.trace("getLecturesByTheme(). Response: {}", () -> response);
        return ResponseEntity.ok().body(response);
    }
}
