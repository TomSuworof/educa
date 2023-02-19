package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<ExerciseDTO>> search(@RequestParam String query) {
        log.debug("search() called. Query: {}", () -> query);
        List<ExerciseDTO> searchResults = searchService.search(query);
        log.trace("search(). Results size: {}", searchResults::size);
        return ResponseEntity.ok(searchResults);
    }
}
