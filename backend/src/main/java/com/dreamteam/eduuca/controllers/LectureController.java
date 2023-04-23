package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.config.ControllerUtils;
import com.dreamteam.eduuca.entities.article.ArticleState;
import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import com.dreamteam.eduuca.payload.request.LectureUploadRequest;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureFullDTO;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureShortDTO;
import com.dreamteam.eduuca.services.article.editor.LectureEditorService;
import com.dreamteam.eduuca.services.article.query.LectureQueryService;
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
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureQueryService lectureQueryService;
    private final LectureEditorService lectureEditorService;

    @GetMapping("")
    @ResponseBody
    public ResponseEntity<PageResponseDTO<LectureShortDTO>> getLecturesPaginated(
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getLecturesPaginated() called. Limit={}, offset={}", limit, offset);
        PageResponseDTO<LectureShortDTO> response = lectureQueryService.getPageByState(ArticleState.PUBLISHED, limit, offset);
        log.trace("getLecturesPaginated(). Response to send: {}", () -> response);
        return ControllerUtils.processPartialResponse(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<LectureFullDTO> getLecture(@PathVariable UUID id, Authentication auth) {
        log.debug("getLecture() called. ID to search: {}", id);
        Lecture lecture = lectureQueryService.getById(id, auth);
        log.trace("getLecture(). Lecture to return: {}", () -> lecture);
        return ResponseEntity.ok().body(new LectureFullDTO(lecture));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<LectureFullDTO> uploadLecture(@RequestBody LectureUploadRequest Lecture, @RequestParam String action, Authentication auth) {
        log.debug("uploadLecture() called. Lecture: {}, action: {}", Lecture, action);
        LectureFullDTO lectureDTO = lectureEditorService.upload(Lecture, ArticleState.getFromAction(action), auth);
        log.trace("uploadLecture(). Result lecture DTO: {}", () -> lectureDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureDTO);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteLecture(@PathVariable UUID id, Authentication auth) {
        log.debug("deleteLecture() called. ID: {}", id);
        lectureQueryService.deleteById(id, auth);
    }
}
