package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.Exercise;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.time.OffsetDateTime;
import java.util.UUID;

@Log4j2
@Getter
@ToString
@EqualsAndHashCode
public class ExerciseDTO implements ObjectDTO {
    protected final UUID id;
    protected final String title;
    protected final String authorName;
    protected final String content;
    protected final String solution;
    protected final OffsetDateTime publicationDate;
    protected final String state;

    public ExerciseDTO(Exercise exercise) {
        log.debug("new ExerciseDTO() called. Exercise: {}", () -> exercise);
        this.id = exercise.getId();
        this.title = exercise.getTitle();
        this.authorName = exercise.getAuthor().getUsername();
        this.content = exercise.getContent();
        this.solution = exercise.getSolution();
        this.publicationDate = exercise.getPublicationDate();
        this.state = exercise.getState().getDescription();
    }
}
