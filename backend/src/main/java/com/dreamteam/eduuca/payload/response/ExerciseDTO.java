package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.Exercise;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExerciseDTO extends ArticleDTO {
    protected final String solution;

    public ExerciseDTO(Exercise exercise) {
        super(exercise);
        this.solution = exercise.getSolution();
    }
}
