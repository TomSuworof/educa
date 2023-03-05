package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.request.AnswerRequest;
import com.dreamteam.eduuca.payload.request.QuestionUploadRequest;
import com.dreamteam.eduuca.payload.response.AnswerResponse;
import com.dreamteam.eduuca.payload.response.QuestionDTO;
import com.dreamteam.eduuca.services.QuestionService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Log4j2
@Controller
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("id") String questionId) {
        log.debug("getQuestion() called. ID={}", questionId);
        QuestionDTO question = questionService.getQuestion(UUID.fromString(questionId));
        log.trace("getQuestion(). Response to send: {}", question);
        return ResponseEntity.ok().body(question);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionUploadRequest questionUploadRequest, Authentication auth) {
        log.debug("addQuestion() called. Request: {}", () -> questionUploadRequest);
        QuestionDTO question = questionService.addQuestion(questionUploadRequest, auth);
        log.trace("addQQuestion(). Response to send: {}", question);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteQuestion(@PathVariable("id") String questionId, Authentication auth) {
        log.debug("deleteQuestion() called. ID={}", questionId);
        questionService.deleteQuestion(UUID.fromString(questionId), auth);
        log.trace("deleteQuestion(). Sending OK");
    }

    @PostMapping("/answer")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<AnswerResponse> answerQuestion(@RequestBody AnswerRequest answerRequest) {
        log.debug("answerQuestion() called. Answer: {}", () -> answerRequest);
        AnswerResponse response = questionService.answerQuestion(answerRequest);
        log.trace("answerQuestion(). Response to send: {}", () -> response);
        return ResponseEntity.ok().body(response);
    }
}
