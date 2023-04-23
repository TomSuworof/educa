package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.article.tag.Tag;
import com.dreamteam.eduuca.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Set<Tag> saveTags(List<String> tags) {
        log.debug("saveTags() called. Tags: {}", () -> tags);
        saveNewTags(tags);
        return tagRepository.findAll().stream().filter(tag -> tags.contains(tag.getName())).collect(Collectors.toSet());
    }

    private void saveNewTags(List<String> tags) {
        log.debug("saveNewTags() called. Tags: {}", () -> tags);

        List<Tag> newTags = tags
                .stream()
                .filter(tag -> tagRepository.findByName(tag).isEmpty())
                .map(Tag::new)
                .toList();
        log.trace("saveNewTags(). New tags: {}", () -> newTags);

        tagRepository.saveAll(newTags);
        log.trace("saveNewTags(). Tags saved successfully");
    }

    public List<Tag> getTagsWithExclusion(List<String> excludedTagNames) {
        log.debug("getTagsWithExclusion() called. Excluded tag names: {}", () -> excludedTagNames);

        List<Tag> tags = tagRepository
                .findAll()
                .stream()
                .filter(tag -> !excludedTagNames.contains(tag.getName()))
                .toList();

        log.trace("getTagsWithExclusion(). Tags after exclusion: {}", () -> tags);

        return tags;
    }

    public List<Tag> getTags(List<String> tagNames) {
        log.debug("getTags() called. Tag names: {}", () -> tagNames);
        return tagRepository.findAll();
    }
}
