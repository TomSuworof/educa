package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.entities.Tag;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.repositories.ExerciseRepository;
import com.dreamteam.eduuca.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TagService {
    private final ExerciseRepository exerciseRepository;
    private final TagRepository tagRepository;

    public Set<Tag> mapToTags(List<String> tags) {
        log.debug("mapToTags() called. Tags: {}", () -> tags);
        saveTags(tags);
        return tagRepository.findAll().stream().filter(tag -> tags.contains(tag.getName())).collect(Collectors.toSet());
    }

    public void saveTags(List<String> tags) {
        log.debug("saveTags() called. Tags: {}", () -> tags);

        List<Tag> newTags = tags
                .stream()
                .filter(tag -> tagRepository.findByName(tag).isEmpty())
                .map(Tag::new)
                .toList();
        log.trace("saveTags(). New tags: {}", () -> newTags);

        tagRepository.saveAll(newTags);
        log.trace("saveTags(). Tags saved successfully");
    }

    public PageResponseDTO<ExerciseDTO> getPageWithExercises(List<String> tagNames, Integer limit, Integer offset) {
        log.debug("getPageWithExercises(). Tag: {}, limit: {}, offset: {}", tagNames, limit, offset);
        Set<UUID> tagIDs = tagNames
                .stream()
                .map(tagRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Tag::getId)
                .collect(Collectors.toSet());

        Page<Exercise> exercises = exerciseRepository.findExercisesByStateAndTags_IdIn(ExerciseState.PUBLISHED, tagIDs, PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

        return new PageResponseDTO<>(
                offset > 0 && exercises.getTotalElements() > 0,
                (offset + limit) < exercises.getTotalElements(),
                exercises.getTotalElements(),
                exercises.getContent().stream().map(ExerciseDTO::new).toList()
        );
    }
}
