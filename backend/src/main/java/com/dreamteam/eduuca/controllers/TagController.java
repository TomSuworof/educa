package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.common.Input;
import com.dreamteam.eduuca.payload.response.TagResponse;
import com.dreamteam.eduuca.services.ModelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Log4j2
@Controller
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final ModelService modelService;

    @PostMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<TagResponse> getTags(@RequestBody Input input) {
        log.debug("getTags() called. Request: {}", () -> input);
        List<String> tags = modelService.getTags(input.text());
        TagResponse response = new TagResponse(input.text(), tags);
        log.trace("getTags(). Response to send: {}", () -> response);
        return ResponseEntity.ok().body(response);
    }
}
