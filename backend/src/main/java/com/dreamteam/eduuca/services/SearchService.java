package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ExerciseRepository exerciseRepository;

    public List<ExerciseDTO> search(String query) {
        log.debug("search() called. Query: {}", () -> query);
        return exerciseRepository
                .fullTextSearch(query)
                .stream()
                .peek(exercise -> log.trace("search(). Found exercise: {}", () -> exercise))
                .map(ExerciseDTO::new)
                .collect(Collectors.toList());
    }
}
