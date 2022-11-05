package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.Exercise;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class ExerciseDTO implements ObjectDTO {
    protected final UUID id;
    protected final String title;
    protected final String content;
    protected final String solution;
    protected final OffsetDateTime publicationDate;
    protected final String state;

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.title = exercise.getTitle();
        this.content = exercise.getContent();
        this.solution = exercise.getSolution();
        this.publicationDate = exercise.getPublicationDate();
        this.state = exercise.getState().getDescription();
    }
}
